# Pets

Pets are a new mechanic for enhancing the combat experience. Pets float around their owners and provide various stat boosts. Obtain them on a crafting table.

## Why this plugin?

Make your server unique by creating your pets from scratch.

## Showcase

![](banner.png)

## Commands

| Name | Description | Permission |
| --- | --- | --- |
| /pets | Opens the pet menu | pets.command.pets |
| /togglepets | Toggles the visibility of your pets | pets.command.togglepets |
| /reloadpets | Reloads the configuration files | pets.command.reloadpets |

## Perks

| Name | Description |
| --- | --- |
| Effect | Adds an effect on the owner |
| Experience | Boosts the amount of experience that the owner receives |
| Thorns | Allows the owner to reflect a portion of the received damage |


## FAQ

### How to get a pet?

Craft pets with the configured recipe.

### How to add a pet to your menu?

Right-click with the pet.

### How to select a pet?

Right-click on it in the menu.

### How to remove a pet from your menu?

Left-click on it in the menu.

### How to level up a pet?

Pets level up as the experience level of their owner increases.

### What are experience boosters?

Experience boosters increase the amount of experience a pet receives.

### What are candies?

Candies instantly give a pet a set amount of experience.

### How to add an experience booster/candy?

Right-click with it.

## Support

If you encounter a bug, please report it on the GitHub issue tracker.

## Configuration

Pets use JSON as their configuration language. Please use https://jsonlint.com/ or similar websites to validate your configuration files, as any syntax errors will result in a crash.

### Placeholders

Lots of strings can have colors and placeholders. Specify colors with `%color_name%` or `%#rrggbb%`. Specify placeholders with `%placeholder_name%`.

### Primitives

| Type | Description | Example |
| --- | --- | --- |
| `int` | An integer from $-2^{31}$ to $2^{31}-1$ | `7` |
| `double` |A double-precision floating point number | `9.7` |
| `boolean` | Either `true` or `false` | `true` |
| `char` | A character | `"a"` |
| `Object` | Anything | `6` |
| `String` | A sequence of characters | `"Hello, World!"` |
| `Expression` | A mathematical expression | `"2cos(2t)"` |
| `UUID` | A universally unique identifier | `"b74413ae-d8a7-4025-8dc7-60ca8b65f979"` |
| `Enchantment` | An enchantment of the form `"minecraft:id"` | `"minecraft:sharpness"` |
| `PotionEffectType` | A potion effect type of the form `"minecraft:id"` | `"minecraft:strength"` |

### Enumerations

| Type | Values |
| --- | --- |
| `Attribute` | https://hub.spigotmc.org/javadocs/spigot/org/bukkit/attribute/Attribute.html |
| `AttributeModifier.Operation` | https://hub.spigotmc.org/javadocs/spigot/org/bukkit/attribute/AttributeModifier.Operation.html |
| `EquipmentSlot` | https://hub.spigotmc.org/javadocs/spigot/org/bukkit/inventory/EquipmentSlot.html |
| `Material` | https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html |

### `List<E>`

```
[E, E, ...]
```

```json
[7, 9]
```

### `Map<K, V>`

```
{
  K: V,
  K: V,
  ...
}
```

```json
{
  "key1": "value1",
  "key2": "value2"
}
```

### `ItemWrapper`

| Name | Type | Description | Optional | Default |
| --- | --- | --- | --- | --- |
| material | `Material` | The material  | No | N/A |
| amount | `int` | The amount | Yes | `1` |
| name | `String` | The name, can have colors  | Yes | Client-side |
| lore | `List<String>` | The lore, can have colors  | Yes | None |
| enchants | `Map<Enchantment, Integer>` | The enchantments with their levels | Yes | None |
| flags | `ItemFlag` | The flags | Yes | None |
| unbreakable | `boolean` | Whether the item should be unbreakable | Yes | `false` |
| attributeModifiers | `Multimap<Attribute, AttributeModifier>` | The attribute modifiers | Yes | None |
| persistentData | `Map<String, Object>` | The persistent data | Yes | None |

```json
{
  "material": "DIAMOND_SWORD",
  "name": "%gold%Excalibur",
  "lore": ["%gold%A legendary sword!"],
  "enchants": {
    "minecraft:sharpness": 5,
    "minecraft:knockback": 2
  },
  "flags": ["HIDE_UNBREAKABLE"],
  "unbreakable": true,
  "attributeModifiers": {
    "GENERIC_MAX_HEALTH": [
      {
        "name": "generic_max_health",
        "amount": 10
      }
    ]
  },
  "persistentData": {
    "id": "EXCALIBUR"
  }
}
```

### `Recipe`

| Name | Type | Description |
| --- | --- | --- |
| shape | `List<String>` | The shape of the recipe of the form `["aaa", "aaa", "aaa"]`, where letters correspond to some ingredient |
| ingredients | `Map<char, Material>` | The ingredients of the recipe |

```json
{
  "shape": [
    "ddd",
    "ddd",
    "ddd"
  ],
  "ingredients": {
    "d": "DIAMOND"
  }
}
```

### `menu.json`

| Name | Type | Description |
| --- | --- | --- |
| title | `String` | The name of the menu |
| size | `int` | The size of the menu, which must be a multiple of 9 |

```json
{
  "title": "Pets",
  "size": 36
}
```

### `position.json`

| Name | Type | Description |
| --- | --- | --- |
| x | `double` | The x coordinate of the pet relative to their owner |
| y | `double` | The y coordinate of the pet relative to their owner |
| z | `double` | The z coordinate of the pet relative to their owner |

```json
{
  "x": 1,
  "y": 1,
  "z": 1
}
```

### `pets.json: List<Pet>`

| Name | Type | Description | Optional |
| --- | --- | --- | --- |
| id | `String` | The identifier | No |
| name | `String` | The name, can have colors and placeholders | No |
| item | `ItemWrapper` | The item | No |
| recipe | `Recipe` | The crafting recipe | No |
| level | `Expression` | How many levels the pet receives based on how many experience levels (x) its owner received | No |
| attributes | `Map<Attribute, Expression>` | The attributes with their amounts based on the pet level (x) | No |
| perk | `String` | The identifier of the perk | Yes |
| maxCandies | `int` | The maximum number of candies | No |

#### Placeholders

| Name | Description | Example |
| --- | --- | --- |
| level | The level | 5 |
| candies | The number of candies | 2 |
| maxCandies | The maximum number of candies | 4 |
| player | The owner | Aregcraft |
| perk | The perk | Regeneration |
| experienceBooster | The experience booster | Decent Experience Booster |
| generic_max_health | The max health | 10 |
| generic_knockback_resistance | The knockback resistance | 1 |
| generic_movement_speed | The movement speed | 0.1 |
| generic_armor | The armor | 5 |
| generic_armor_toughness | The armor toughness | 3 |
| generic_attack_knockback | The attack knockback | 1 |

```json
[
  {
    "id": "LION",
    "name": "%green%[%level%] %player%'s Lion",
    "head": "6b3a8ce66dc3927bb5482b29e936b39d24589f91e997bb3dfd567396e871120",
    "item": {
      "material": "PLAYER_HEAD",
      "name": "%green%[%level%] Lion",
      "lore": [
        "%dark_gray%Roar...",
        "",
        "%gray%%candies%/%maxCandies% candies used!",
        "",
        "%gray%When selected:",
        "%dark_green% %experienceBooster%",
        "%dark_green% %perk% Perk",
        "%dark_green% %generic_max_health% Max Health",
        "%dark_green% %generic_attack_damage% Attack Damage",
        "%dark_green% %generic_armor% Armor"
      ]
    },
    "recipe": {
      "shape": [
        "ggg",
        "geg",
        "ggg"
      ],
      "ingredients": {
        "g": "GOLD_BLOCK",
        "e": "EGG"
      }
    },
    "level": "x",
    "attributes": {
      "GENERIC_MAX_HEALTH": "x",
      "GENERIC_ATTACK_DAMAGE": "x / 10",
      "GENERIC_ARMOR": "x / 10"
    },
    "perk": "EXPERIENCE",
    "maxCandies": 3
  },
  {
    "id": "ELEPHANT",
    "name": "%gray%[%level%] %player%'s Elephant",
    "head": "7071a76f669db5ed6d32b48bb2dba55d5317d7f45225cb3267ec435cfa514",
    "item": {
      "material": "PLAYER_HEAD",
      "name": "%gray%[%level%] Elephant",
      "lore": [
        "%dark_gray%Trumpet...",
        "",
        "%gray%%candies%/%maxCandies% candies used!",
        "",
        "%gray%When selected:",
        "%dark_green% %experienceBooster%",
        "%dark_green% %perk% Perk",
        "%dark_green% %generic_max_health% Max Health",
        "%dark_green% %generic_armor% Armor",
        "%dark_green% %generic_attack_speed% Attack Speed"
      ]
    },
    "recipe": {
      "shape": [
        "odo",
        "ded",
        "odo"
      ],
      "ingredients": {
        "o": "OBSIDIAN",
        "d": "DIAMOND_BLOCK",
        "e": "EGG"
      }
    },
    "level": "x / 2",
    "attributes": {
      "GENERIC_MAX_HEALTH": "x",
      "GENERIC_ATTACK_SPEED": "x / 100",
      "GENERIC_ARMOR": "x / 5"
    },
    "perk": "REGENERATION",
    "maxCandies": 4
  },
  {
    "id": "CHEETAH",
    "name": "%yellow%[%level%] %player%'s Cheetah",
    "head": "1553f8856dd46de7e05d46f5fc2fb58eafba6829b11b160a1545622e89caaa33",
    "item": {
      "material": "PLAYER_HEAD",
      "name": "%yellow%[%level%] Cheetah",
      "lore": [
        "%dark_gray%Chirrs...",
        "",
        "%gray%%candies%/%maxCandies% candies used!",
        "",
        "%gray%When selected:",
        "%dark_green% %experienceBooster%",
        "%dark_green% %perk% Perk",
        "%dark_green% %generic_movement_speed% Movement Speed",
        "%dark_green% %generic_attack_damage% Attack Damage",
        "%dark_green% %generic_attack_speed% Attack Speed"
      ]
    },
    "recipe": {
      "shape": [
        "igi",
        "geg",
        "igi"
      ],
      "ingredients": {
        "i": "IRON_BLOCK",
        "g": "GOLD_BLOCK",
        "e": "EGG"
      }
    },
    "level": "x / 2",
    "attributes": {
      "GENERIC_MOVEMENT_SPEED": "x / 1000",
      "GENERIC_ATTACK_SPEED": "x / 100",
      "GENERIC_ATTACK_DAMAGE": "x / 5"
    },
    "perk": "THORNS",
    "maxCandies": 4
  }
]
```

### `experience_boosters.json: List<ExperienceBooster>`

| Name | Type | Description |
| --- | --- | --- |
| id | `String` | The identifier |
| item | `ItemWrapper` | The item |
| recipe | `Recipe` | The recipe |
| boost | `Expression` | The number of levels to add to the earned ones (x) |

```json
[
  {
    "id": "WEAK_EXPERIENCE_BOOSTER",
    "item": {
      "material": "EXPERIENCE_BOTTLE",
      "name": "%green%Weak Experience Booster",
      "lore": [
        "%gray%Increases the amount of pet experience",
        "%gray%you receive by 50%."
      ]
    },
    "recipe": {
      "shape": [
        "gdg",
        "ded",
        "gdg"
      ],
      "ingredients": {
        "g": "GOLD_INGOT",
        "d": "DIAMOND",
        "e": "EMERALD"
      }
    },
    "boost": "0.5x"
  },
  {
    "id": "DECENT_EXPERIENCE_BOOSTER",
    "item": {
      "material": "EXPERIENCE_BOTTLE",
      "name": "%blue%Decent Experience Booster",
      "lore": [
        "%gray%Increases the amount of pet experience",
        "%gray%you receive by 100%."
      ]
    },
    "recipe": {
      "shape": [
        "gdg",
        "ded",
        "gdg"
      ],
      "ingredients": {
        "g": "GOLD_BLOCK",
        "d": "DIAMOND_BLOCK",
        "e": "EMERALD_BLOCK"
      }
    },
    "boost": "x"
  },
  {
    "id": "STRONG_EXPERIENCE_BOOSTER",
    "item": {
      "material": "EXPERIENCE_BOTTLE",
      "name": "%red%Strong Experience Booster",
      "lore": [
        "%gray%Increases the amount of pet experience",
        "%gray%you receive by 200%."
      ]
    },
    "recipe": {
      "shape": [
        "ded",
        "ene",
        "ded"
      ],
      "ingredients": {
        "d": "DIAMOND_BLOCK",
        "e": "EMERALD_BLOCK",
        "n": "NETHER_STAR"
      }
    },
    "boost": "2x"
  }
]
```

### `candies.json: List<Candy>`

| Name | Type | Description |
| --- | --- | --- |
| id | `String` | The identifier |
| item | `ItemWrapper` | The item |
| recipe | `Recipe` | The recipe |
| experience | `double` | The number of levels to add |

```json
[
  {
    "id": "SMALL_CANDY",
    "item": {
      "material": "SUGAR",
      "name": "%green%Small Candy",
      "lore": [
        "%gray%Instantly adds two levels to your",
        "%gray%selected pet."
      ]
    },
    "recipe": {
      "shape": [
        "sgs",
        "gdg",
        "sgs"
      ],
      "ingredients": {
        "s": "SUGAR",
        "g": "GOLD_INGOT",
        "d": "DIAMOND"
      }
    },
    "experience": 2
  },
  {
    "id": "MEDIUM_CANDY",
    "item": {
      "material": "SUGAR",
      "name": "%blue%Medium Candy",
      "lore": [
        "%gray%Instantly adds four levels to your",
        "%gray%selected pet."
      ]
    },
    "recipe": {
      "shape": [
        "sgs",
        "gdg",
        "sgs"
      ],
      "ingredients": {
        "s": "SUGAR",
        "g": "GOLD_BLOCK",
        "d": "DIAMOND_BLOCK"
      }
    },
    "experience": 4
  },
  {
    "id": "BIG_CANDY",
    "item": {
      "material": "SUGAR",
      "name": "%red%Big Candy",
      "lore": [
        "%gray%Instantly adds eight levels to your",
        "%gray%selected pet."
      ]
    },
    "recipe": {
      "shape": [
        "sds",
        "dnd",
        "sds"
      ],
      "ingredients": {
        "s": "SUGAR",
        "d": "DIAMOND_BLOCK",
        "n": "NETHER_STAR"
      }
    },
    "experience": 8
  }
]
```

### `perks.json: List<Perk>`

| Name | Type | Description |
| --- | --- | --- |
| base | `String` | The base from which to inherit other properties |
| id | `String` | The identifier |
| name | `String` | The name, can have colors |

```json
[
  {
    "base": "Effect",
    "id": "REGENERATION",
    "name": "%red%Regeneration",
    "type": "minecraft:regeneration",
    "amplifier": 0,
    "hideParticles": true
  },
  {
    "base": "Experience",
    "id": "EXPERIENCE",
    "name": "%green%Experience",
    "bonus": "0.5x"
  },
  {
    "base": "Thorns",
    "id": "THORNS",
    "name": "%green%Thorns",
    "reflect": "0.125x"
  }
]
```

### Bases

#### Effect

| Name | Type | Description |
| --- | --- | --- |
| type | `PotionEffectType` | The effect type |
| amplifier | `int` | The effect amplifier |
| hideParticles | `boolean` | Whether to hide the effect particles |

#### Experience

| Name | Type | Description |
| --- | --- | --- |
| boost | `Expression` | The amount of experience to add to the earned ones (x) |

#### Thorns

| Name | Type | Description |
| --- | --- | --- |
| reflect | `Expression` | How much damage to reflect on the attacker based on the received damage (x) |