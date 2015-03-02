# Cloud Launch Demo Spring Boot Application

This is an app which displays the health and status of the app, the running instances of the app, as well as the RAM and Disk space that the app uses. The app has a check for updates feature that is activated when you commit a change to the GitHub repository and refresh the page. Clicking the update will trigger a Jenkins job which does a blue-green deploy of the app and refreshes the page.

## Running on [Pivotal Web Services] [pws]

Log in.

```bash
cf login -a https://api.run.pivotal.io
```

Target your org / space

```bash
cf target -o myorg -s myspace
```

Create a GitHub access token following the directions [here] [token]. You will only need the repo and public_repo scopes selected. You will need to copy the created token to your clipboard for use in a later step. 

Create a Jenkins job and configure it to match your forked GitHub repository and your new host name.

Create three user-provided-services using the interactive method based on docs provided [here] [cups]. The services should be called "cc", "jenkins", and "github." You will bind these services to the app in a later step. 

The user-provided service "cc" should pass in the following parameter names: url, user, and password.

```bash
cf cups cc -p "url, user, password"
```

The cf CLI will prompt for the paramaters in turn.

```bash
url> https://api.run.pivotal.io
user> your PWS email
password> your PWS password
```

The user-provided service "jenkins" should pass in the following parameter names: baseUrl, jobName, user, and password.

```bash
cf cups jenkins -p "baseUrl, jobName, user, password"
```

The cf CLI prompts you for the paramaters.

```bash
baseUrl> http://url of your Jenkins job home page
jobName> name of your Jenkins job
user> Jenkins username
password> Jenkins password
```

The user-provided service "github" should pass in the following parameter names: accessToken, clientId, clientSecret, repoName, and repoOwner.

```bash
cf cups github -p "accessToken, clientId, clientSecret, repoName, repoOwner"
```

The cf CLI once again prompts you for the paramaters. The accessToken can be created by following the directions [here] [token].

```bash
accessToken> the token you created 
clientId> your clientId
clientSecret> your clientSecret
repoName> the name of the repo when you forked cloud-launch-demo
repoOwner> your GitHub username
```

Build the app.

```bash
mvn package
```

Push the app and specify the number of instances, memory limit, your unique subdomain, and add the flag --no-start to prevent building the droplet at this point.

```bash
cf push <app_name> -i 1 -m 512M -n <unique_subdomain> --no-start
```

Bind the user-provided services to the app.

```bash
cf bs <app_name> cc
cf bs <app_name> jenkins
cf bs <app_name> github
```

Verify that the user-provided services have bound to the app.

```bash
cf services
```

Now start the app.

```bash
cf start <app_name>
```

## Usage Instructions

Now that the app is started, find your app in the org and space you targeted within your [Pivotal Web Services] [pws] account and click the link under the app.

Once running, click the "kill the active instance!" link and refresh the page.

Now use the CF CLI to scale the app out to three instances. 

```bash
cf scale <app_name> -i 3
```

Click the "kill the active instance!" link, refresh the page and note what happens.

Scale the app back down to one instance.

```bash
cf scale <app_name> -i 1
```

Test the update portion of the app by committing a change to the GitHub repository. This can be done by checking in a minor change to the text file of the app and submitting a pull request to add that to the master copy. Once the pull request is merged and the app page is refreshed the Update button will now be active. Clicking the Update button will trigger the Jenkins job which does a blue-green deploy of the app and refreshes the page with the updated text file.
A basic GitHub guide for committing changes and creating and merging pull requests can be found [here] [commit].


[pws]:https://run.pivotal.io
[token]:https://help.github.com/articles/creating-an-access-token-for-command-line-use/ 
[cups]:http://docs.pivotal.io/pivotalcf/devguide/services/user-provided.html#user-cups
[commit]:https://guides.github.com/activities/hello-world/
