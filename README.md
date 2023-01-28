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

<!-- <perks> -->
<!-- </perks> -->

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

Visit the official discord server [https://discord.gg/JaeqU4HU](https://discord.gg/JaeqU4HU).

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
| `EntityType` | https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/entity/EntityType.html |

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

<!-- <menu_json> -->
<!-- </menu_json> -->

### `position.json`

| Name | Type | Description |
| --- | --- | --- |
| x | `double` | The x coordinate of the pet relative to their owner |
| y | `double` | The y coordinate of the pet relative to their owner |
| z | `double` | The z coordinate of the pet relative to their owner |

<!-- <position_json> -->
<!-- </position_json> -->

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

<!-- <pets_json> -->
<!-- </pets_json> -->

### `experience_boosters.json: List<ExperienceBooster>`

| Name | Type | Description |
| --- | --- | --- |
| id | `String` | The identifier |
| item | `ItemWrapper` | The item |
| recipe | `Recipe` | The recipe |
| boost | `Expression` | The number of levels to add to the earned ones (x) |

<!-- <experience_boosters_json> -->
<!-- </experience_boosters_json> -->

### `candies.json: List<Candy>`

| Name | Type | Description |
| --- | --- | --- |
| id | `String` | The identifier |
| item | `ItemWrapper` | The item |
| recipe | `Recipe` | The recipe |
| experience | `double` | The number of levels to add |

<!-- <candies_json> -->
<!-- </candies_json> -->

### `perks.json: List<Perk>`

| Name | Type | Description |
| --- | --- | --- |
| base | `String` | The base from which to inherit other properties |
| id | `String` | The identifier |
| name | `String` | The name, can have colors |

<!-- <perks_json> -->
<!-- </perks_json> -->

### Bases

<!-- <bases> -->
<!-- </bases> -->