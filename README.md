# hs-plugin-minecraft
Universal plugin for integration with Minecraft servers.\
The current version of this plugin is: **0.0.1** and remains under constant development.\
<br>
You can download new or old versions in the [Releases](https://github.com/hs-org/hs-plugin-minecraft/releases) tab.

## Table of Contents
* [Requirements](#requirements)
* [Starting to set up](#starting-to-set-up)
  * [Credentials](#credentials)
  * Translation
* [Collected data](#collected-data)
* [Support](#support)
* [Contributing](#contributing)

## Requirements
This plug-in will compile and create .jar files for the following server platforms:
* Bukkit/Spigot
* ~~Bungeecord~~

## Starting to set up
All modules include a single configuration file `config.yml`.\
We offer some basic tweaks so you can adapt the plugin to your server.

### Credentials
In order for the plugin to communicate with HappyShop's APIs, some store credentials must be properly configured.\
As with our SDKs, you must provide your store credentials.
In the `api >`**`credentials`** section of the configuration file you will find the following:
```yaml
api:
  endpoint: https://api.happyshop.net
  credentials:
    store: <your-store-token>
    access-key: <your-api-access-key>
    secret: <your-api-secret>
```
Your and your store credentials can be found in your account developer settings and store dashboard on the official HappyShop website. The above values are:
* **`store`**: A unique token generated solely for your store.
* **`api-access-key`**: The store access key.
* **`api-secret`**: The store secret.

## Collected data
For the plugin to work as intended we need to collect some information about your server and players.\
But don't worry, your data will be saved and everything we collect will be visible to you visually.\
<br>
What we will collect:
* Name and version of the protocol. (e.g. Bukkit 1.8.8)
* Server address and port.
* Maximum players and number of players online.
* When the plugin was started and its uptime.

## Support
As with all HappyShop projects that are open source, we support via our Discord server.\
You can also open an Issue and it will be answered when possible.

## Contributing
You can contribute to any project module.\
But you need to meet some requirements for that, see [How to contribute](#).