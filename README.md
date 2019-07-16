# ItemBank 0.3
A simple item based currency for your favorite economy plugin.  
**Economy plugin** and **Vault** required!  
No **commands** !
***
:warning: You need an economy plugin, (for example [EssentialsX](https://www.spigotmc.org/resources/essentialsx.9089/) ), and [Vault](https://dev.bukkit.org/projects/vault) to use this plugin.
***

***
:info: v0.3 is for SPIGOT 1.14.3
***

## Features
- Easy setup
- No commands: Only signs
- Deposit/Withdraw
- ATM signs (Deposit and withdraw on the same sign)
- Configurable item/value/withdraw options etc...

## Summary 

- [Installation](#installation)
- [Usage](#usage)
    - [OnClick](#onclick-feature)
    - [Deposit sign](#deposit-sign)
    - [Withdrawn sign](#withdraw-sign)
    - [ATM sign](#atm-sign)
- [Commands and permissions](#commands-and-permissions)
    - [Commands](#commands)
    - [Permissions](#permissions)
- [Configuration file](#configuration-file)

## Installation 
- Download latest build
- Copy it in your plugin directory
- Run/Reload your server!

## Usage
#### OnClick Feature
**Right click** with the defined item currency to deposit.
![right_click1](https://image.prntscr.com/image/PazdbnK_QnWq7DRW_ER2vQ.png)
![right_click2](https://image.prntscr.com/image/YVj-VTzgRKeQnrQpn5yjpQ.png)

#### Deposit sign
Place a sign with:
```
1 - [deposit]
2 - 
3 - Optional name
4 - 
```
![deposit1](https://image.prntscr.com/image/49Cajh2KTDOl1-ZKw6JHGg.png)

![deposit2](https://image.prntscr.com/image/FF-lDbitRUm0dX1yL3HFWA.png)
**Right click** on it to deposit **ALL** your items (currency).

#### Withdraw sign
Place a sign with:
```
1 - [withdraw]
2 - 
3 - Optional name
4 - 
```
![w1](https://image.prntscr.com/image/-0o4KhESTU_6JDF1Ag-LWw.png)

![w2](https://image.prntscr.com/image/NoeRXSi7RdSFpPb3ALfCjw.png)
**Right click** on it and select an option to withdraw **n** items.
![withdraw](https://image.prntscr.com/image/lrsJXBmIRni4xStatRrdoA.png)

#### ATM sign
Place a sign with:
```
1 - [ATM]
2 - 
3 - Optional name and very useless
4 - 
```
![image](https://cdn.discordapp.com/attachments/194807204916756480/348444397274398720/itembank.gif)  
**Shift** + **right click** to *switch* between deposit and withdraw mode.  
Then follow the previous instructions.

## Commands and permissions

#### Commands
| Command | Permission | Description |
|---------|------------|-------------|
| /ib help | itembank.commands.ib | Display help message |
| /ib info | itembank.commands.ib | Display informations about plugin |
| /ib admin | itembank.commands.ib.admin | Display admin help message |
| /ib config | itembank.commands.ib.admin | Display configuration |
| /ib debug | itembank.commands.ib.admin | uh.. 3 items to test your configuration |

Alias: /itembank /itemb /ibank

#### Permissions
| Permission | Description |
|------------|-------------|
| itembank.use.click | Allow use of OnClick feature |
| itembank.use.sign.deposit | Allow use of deposit signs |
| itembank.use.sign.withdraw | Allow use of withdraw signs |
| itembank.use.sign.atm | Allow use of ATM signs |
| itembank.create.sign.deposit | Allow creation of deposit signs |
| itembank.create.sign.withdraw | Allow creation of withdraw signs |
| itembank.create.sign.atm | Allow creation of ATM signs |
| itembank.break.sign | Allow destruction of signs |
| itembank.commands.ib | Allow access to ib normal commands |
| itembank.commands.ib.admin | Allow access to ib admin commands |

And **wildcards**:

| Node |
|------|
| itembank.* |
| itembank.use.* |
| itembank.use.sign.* |
| itembank.create.* |
| itembank.commands.* |



## Configuration file
Default config file:
```YAML
item: GOLD_NUGGET
value: 1
on_click: true
sign: true
withdraw:
  value_1: 1
  value_2: 5
  value_3: 10
  value_4: 50
  value_5: 100
  value_6: 500
  value_7: 1000
  value_8: 1500
  value_9: 2000
```  
Choose the item currency:
```
item: GOLD_NUGGET
```
Choose the value of the item currency:
```  
value: 1
```
Enable/Disable the right click feature:
```  
on_click: true
```
Enable/Disable signs:
```
sign: true
```
Define the withdraw options:
```
withdraw:
  value_1: 1
  value_2: 5
  value_3: 10
  value_4: 50
  value_5: 100
  value_6: 500
  value_7: 1000
  value_8: 1500
  value_9: 2000
```
![withdraw](https://image.prntscr.com/image/lrsJXBmIRni4xStatRrdoA.png)