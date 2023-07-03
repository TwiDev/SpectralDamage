![spectraldamagelogo](https://s12.gifyu.com/images/SQNja.png)

# SpectralDamage
**Asynchronous client-side (packets) damage indicator spigot plugin**
___

[![forthebadge made-with-java](https://forthebadge.com/images/badges/made-with-java.svg)](https://java.com/)

>SpectralDamage is an open collaboration project by [TwiDev](https://github.com/TwiDev).

[![Download](https://custom-icon-badges.herokuapp.com/badge/-Download-blue?style=for-the-badge&logo=DOWNLOAD&logoColor=white "Download")](https://www.spigotmc.org/resources/spectraldamage-1-8-1-20-free-damage-indicator-plugin-no-dependencies.110551/)  
(Via spigotmc.org)

**Supported versions: 1.8.X - 1.20**

## What is SpectralDamage ?

Spectral Damage is a minecraft spigot plugin that allows you to display in the form of an animated hologram the damage that a player inflicts on a mob or another player

## API
Download API Jar [here](https://github.com/TwiDev/SpectralDamage/releases)

Maven / gradle : soon

### Examples

<u>Get Damage Type instante</u>:

```java
    private static DamageTypeFactory NORMAL_DAMAGE;

    @Override
    public void onEnable() {
        NORMAL_DAMAGE = SpectralDamage.getInstance().getDamageTypeFactory(DamageType.NORMAL);

        getCommand("damage").setExecutor(this);
    }
```

<u>Spawn Damage Indicator</u>

```java
SpectralDamage.getInstance().spawnDamageIndicator(player.getLocation(), NORMAL_DAMAGE, 10, true);
```




