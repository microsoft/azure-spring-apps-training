# 00 - Alternative Local Environment Setup Using Docker

__This guide is part of the [Azure Spring Cloud training](../README.md)__

If you do not have a pre-existing development environment for Java, we have provided a Docker container image with all the dependencies. This image can be used in conjunction with Visual Studio code to provide a sufficient development environment for this training course.

---

## Running the container image

A docker image containing all of the pre-requisites is available. You'll need:

* Docker Desktop.
  > If you need to install Docker...
  > - [Click here](https://download.docker.com/win/stable/Docker%20Desktop%20Installer.exe) for Windows
  > - [Click here](https://download.docker.com/mac/stable/Docker.dmg) for Mac

* Visual Studio Code ([Download here](https://code.visualstudio.com/?WT.mc_id=azurespringcloud-github-judubois))
* ["Remote - Containers" extension](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers&WT.mc_id=azurespringcloud-github-judubois) for Visual Studio Code

With Docker installed, run (in Bash or PowerShell with administrator privileges)

```bash
docker run -p 8080:8080 -d azurejavalab.azurecr.io/azurejavalab:latest
```

## Preparing Visual Studio Code

Visual Studio code makes it easy to edit files and run commands inside a container all in one single, fluid interface. If you don't have VS Code installed, [install it now](https://code.visualstudio.com).

With Visual Studio Code installed, open the page for the [Remote - Containers](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers&WT.mc_id=azurespringcloud-github-judubois) extension and click "Install". If prompted to allow the browser to open Visual Studio Code, allow it.

Visual Studio Code will open, showing the description of the Remote Containers extension. Click on the green install button on that page to install the extension:

![Visual Studio Code extension page](media/01-remote-containers-extension-install-page.png)

If an "uninstall" button appears in place of the "Install" button, then the extension is already installed and you can continue.

## Connecting to the Container

With the container running on your machine, click on the "Remote Explorer" button on the left toolbar in Visual Studio Code:

![Remote explorer button](media/02-remote-explorer-button.png)

The Remote Explorer pane will appear, and you should see the lab container in it. Right-click on the container and click "Connect to Container".

![Remote explorer pane](media/03-remote-explorer-pane.png)

A new window will open. Press CTRL+SHIFT+P (Mac: ⇧⌘P) to open the command bar and Enter "Open Folder". Select the "File: Open Folder" command. In the subsequent dialog, enter `/lab`:

![Open Folder](media/04-open-folder.png)

![Select the remote folder](media/05-selecting-folder.png)

Click Ok.

You should now see the contents of the `lab` folder on the left side pane. There isn't much to see (yet), as the folder is empty.

Last, press CTRL+SHIFT+\` (Mac: ⌃⇧`) or choose "Terminal: Create New Integrated Terminal" from the command pallete.

Your Visual Studio Code window should now look like this:

![Visual Studio Code - Remote](media/06-container-vscode.png)

You can use the integrated shell on the bottom right for any command you need to run as part of this lab. Any files you need to edit, you can open from the panel on the left or by pressing CTRL+P.
