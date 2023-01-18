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
| `String` | A sequence of characters | `"Hello, World!"` |
| `Expression` | A mathematical expression | `"2cos(2t)"` |

### Enumerations

| Type | Values |
| --- | --- |
| `Attribute` | https://hub.spigotmc.org/javadocs/spigot/org/bukkit/attribute/Attribute.html |

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

| Name | Type | Description |
| --- | --- | --- |
| id | `String` | The identifier |
| name | `String` | The name |
| item | `ItemWrapper` | The item |
| recipe | `Recipe` | The crafting recipe |
| level | `Expression` | How many levels the pet receives based on how many experience levels (x) its owner received |
| attributes | `Map<Attribute, Expression>` | The attributes with their amounts based on the pet level (x) |

#### Placeholders

| Name | Description | Example |
| --- | --- | --- |
| level | The level | 5 |
| player | The owner | Aregcraft |
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
        "%gray%When selected:",
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
    }
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
        "%gray%When selected:",
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
    }
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
        "%gray%When selected:",
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
    }
  }
]
```