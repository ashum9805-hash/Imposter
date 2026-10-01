package com.example.data

import com.example.model.WordItem
import java.security.SecureRandom

object WordRepository {

  val CATEGORIES = listOf(
    "Superheroes",
    "Food & Drink",
    "Pop Culture & Movies",
    "Animals & Wildlife",
    "Everyday Objects",
    "Places & Travel",
    "Science & Tech",
    "Occupations",
    "Sports & Games",
    "Fantasy & Mystery"
  )

  private val defaultWords = listOf(
    // Superheroes
    WordItem(
      id = "hero_1",
      word = "Superman",
      category = "Superheroes",
      hints = listOf("Cape", "Fly", "Blue"),
      distractors = listOf("Batman", "Flash", "Thor")
    ),
    WordItem(
      id = "hero_2",
      word = "Batman",
      category = "Superheroes",
      hints = listOf("Night", "Black", "Rich"),
      distractors = listOf("Iron Man", "Daredevil", "Spiderman")
    ),
    WordItem(
      id = "hero_3",
      word = "Spiderman",
      category = "Superheroes",
      hints = listOf("Red", "Jump", "City"),
      distractors = listOf("Batman", "Flash", "Ant-Man")
    ),
    WordItem(
      id = "hero_4",
      word = "Iron Man",
      category = "Superheroes",
      hints = listOf("Metal", "Armor", "Fly"),
      distractors = listOf("War Machine", "Cyborg", "Batman")
    ),
    WordItem(
      id = "hero_5",
      word = "Hulk",
      category = "Superheroes",
      hints = listOf("Green", "Big", "Angry"),
      distractors = listOf("Thing", "Juggernaut", "Colossus")
    ),
    WordItem(
      id = "hero_6",
      word = "Thor",
      category = "Superheroes",
      hints = listOf("Hammer", "Thunder", "God"),
      distractors = listOf("Loki", "Odin", "Superman")
    ),
    WordItem(
      id = "hero_7",
      word = "Wonder Woman",
      category = "Superheroes",
      hints = listOf("Gold", "Shield", "Warrior"),
      distractors = listOf("Supergirl", "Captain Marvel", "Batgirl")
    ),
    WordItem(
      id = "hero_8",
      word = "Flash",
      category = "Superheroes",
      hints = listOf("Fast", "Lightning", "Run"),
      distractors = listOf("Quicksilver", "Sonic", "Superman")
    ),
    WordItem(
      id = "hero_9",
      word = "Wolverine",
      category = "Superheroes",
      hints = listOf("Claws", "Metal", "Wild"),
      distractors = listOf("Beast", "Sabretooth", "Deadpool")
    ),
    WordItem(
      id = "hero_10",
      word = "Captain America",
      category = "Superheroes",
      hints = listOf("Shield", "Star", "Soldier"),
      distractors = listOf("Bucky", "Falcon", "Superman")
    ),
    WordItem(
      id = "hero_11",
      word = "Black Panther",
      category = "Superheroes",
      hints = listOf("Cat", "King", "Black"),
      distractors = listOf("Batman", "Wolverine", "Hawkeye")
    ),

    // Food & Drink
    WordItem(
      id = "fd_choc",
      word = "Chocolate",
      category = "Food & Drink",
      hints = listOf("Sweet", "Brown", "Gift"),
      distractors = listOf("Fudge", "Caramel", "Marshmallow")
    ),
    WordItem(
      id = "fd_1",
      word = "Espresso",
      category = "Food & Drink",
      hints = listOf("Morning", "Dark", "Cup"),
      distractors = listOf("Cappuccino", "Matcha Latte", "Hot Chocolate")
    ),
    WordItem(
      id = "fd_2",
      word = "Pizza",
      category = "Food & Drink",
      hints = listOf("Round", "Cheese", "Hot"),
      distractors = listOf("Calzone", "Lasagna", "Flatbread")
    ),
    WordItem(
      id = "fd_3",
      word = "Sushi",
      category = "Food & Drink",
      hints = listOf("Rice", "Cold", "Fish"),
      distractors = listOf("Sashimi", "Ramen", "Poke Bowl")
    ),
    WordItem(
      id = "fd_4",
      word = "Pancake",
      category = "Food & Drink",
      hints = listOf("Breakfast", "Sweet", "Flat"),
      distractors = listOf("Waffle", "Crepe", "French Toast")
    ),
    WordItem(
      id = "fd_5",
      word = "Ice Cream",
      category = "Food & Drink",
      hints = listOf("Cold", "Sweet", "Summer"),
      distractors = listOf("Gelato", "Frozen Yogurt", "Sorbet")
    ),
    WordItem(
      id = "fd_6",
      word = "Burger",
      category = "Food & Drink",
      hints = listOf("Meat", "Fast", "Bread"),
      distractors = listOf("Hot Dog", "Sandwich", "Taco")
    ),
    WordItem(
      id = "fd_7",
      word = "Tacos",
      category = "Food & Drink",
      hints = listOf("Spicy", "Crunchy", "Mexican"),
      distractors = listOf("Burrito", "Quesadilla", "Enchilada")
    ),

    // Pop Culture & Movies
    WordItem(
      id = "pop_1",
      word = "Lightsaber",
      category = "Pop Culture & Movies",
      hints = listOf("Glow", "Space", "Sword"),
      distractors = listOf("Phaser", "Magic Wand", "Laser Blaster")
    ),
    WordItem(
      id = "pop_2",
      word = "Hogwarts",
      category = "Pop Culture & Movies",
      hints = listOf("Castle", "Magic", "School"),
      distractors = listOf("Narnia", "Middle-earth", "Westeros")
    ),
    WordItem(
      id = "pop_3",
      word = "Titanic",
      category = "Pop Culture & Movies",
      hints = listOf("Water", "Cold", "Ship"),
      distractors = listOf("Poseidon", "Black Pearl", "Nautilus")
    ),
    WordItem(
      id = "pop_4",
      word = "James Bond",
      category = "Pop Culture & Movies",
      hints = listOf("Suit", "Car", "Secret"),
      distractors = listOf("Ethan Hunt", "Jason Bourne", "Sherlock")
    ),

    // Animals & Wildlife
    WordItem(
      id = "anim_1",
      word = "Chameleon",
      category = "Animals & Wildlife",
      hints = listOf("Color", "Green", "Tree"),
      distractors = listOf("Gecko", "Iguana", "Octopus")
    ),
    WordItem(
      id = "anim_2",
      word = "Penguin",
      category = "Animals & Wildlife",
      hints = listOf("Ice", "Bird", "Cold"),
      distractors = listOf("Puffin", "Albatross", "Pelican")
    ),
    WordItem(
      id = "anim_3",
      word = "Kangaroo",
      category = "Animals & Wildlife",
      hints = listOf("Jump", "Pouch", "Wild"),
      distractors = listOf("Koala", "Wallaby", "Wombat")
    ),
    WordItem(
      id = "anim_4",
      word = "Octopus",
      category = "Animals & Wildlife",
      hints = listOf("Sea", "Deep", "Water"),
      distractors = listOf("Squid", "Jellyfish", "Cuttlefish")
    ),
    WordItem(
      id = "anim_5",
      word = "Giraffe",
      category = "Animals & Wildlife",
      hints = listOf("Tall", "Yellow", "Tree"),
      distractors = listOf("Zebra", "Elephant", "Camel")
    ),
    WordItem(
      id = "anim_6",
      word = "Owl",
      category = "Animals & Wildlife",
      hints = listOf("Night", "Bird", "Eyes"),
      distractors = listOf("Hawk", "Bat", "Falcon")
    ),

    // Everyday Objects
    WordItem(
      id = "obj_paper",
      word = "Paper",
      category = "Everyday Objects",
      hints = listOf("White", "Flat", "Write"),
      distractors = listOf("Cardboard", "Notebook", "Canvas")
    ),
    WordItem(
      id = "obj_1",
      word = "Umbrella",
      category = "Everyday Objects",
      hints = listOf("Rain", "Wet", "Water"),
      distractors = listOf("Raincoat", "Parasol", "Hat")
    ),
    WordItem(
      id = "obj_2",
      word = "Mirror",
      category = "Everyday Objects",
      hints = listOf("Glass", "Wall", "Look"),
      distractors = listOf("Window", "Portrait", "Camera Screen")
    ),
    WordItem(
      id = "obj_3",
      word = "Clock",
      category = "Everyday Objects",
      hints = listOf("Time", "Numbers", "Circle"),
      distractors = listOf("Hourglass", "Stopwatch", "Calendar")
    ),
    WordItem(
      id = "obj_4",
      word = "Backpack",
      category = "Everyday Objects",
      hints = listOf("Bag", "Heavy", "Travel"),
      distractors = listOf("Duffel Bag", "Briefcase", "Tote Bag")
    ),
    WordItem(
      id = "obj_5",
      word = "Headphones",
      category = "Everyday Objects",
      hints = listOf("Music", "Sound", "Ear"),
      distractors = listOf("Earplugs", "Speakers", "Microphone")
    ),
    WordItem(
      id = "obj_6",
      word = "Toothbrush",
      category = "Everyday Objects",
      hints = listOf("Morning", "Clean", "Mouth"),
      distractors = listOf("Dental Floss", "Hairbrush", "Razor")
    ),

    // Places & Travel
    WordItem(
      id = "plc_1",
      word = "Eiffel Tower",
      category = "Places & Travel",
      hints = listOf("Tall", "Metal", "Paris"),
      distractors = listOf("Arc de Triomphe", "Big Ben", "Colosseum")
    ),
    WordItem(
      id = "plc_2",
      word = "Airport",
      category = "Places & Travel",
      hints = listOf("Sky", "Travel", "Wait"),
      distractors = listOf("Train Station", "Cruise Port", "Bus Terminal")
    ),
    WordItem(
      id = "plc_3",
      word = "Grand Canyon",
      category = "Places & Travel",
      hints = listOf("Rock", "Deep", "Red"),
      distractors = listOf("Yellowstone", "Yosemite", "Death Valley")
    ),
    WordItem(
      id = "plc_4",
      word = "Pyramids of Giza",
      category = "Places & Travel",
      hints = listOf("Sand", "Stone", "Old"),
      distractors = listOf("Petra", "Stonehenge", "Machu Picchu")
    ),
    WordItem(
      id = "plc_5",
      word = "Library",
      category = "Places & Travel",
      hints = listOf("Quiet", "Books", "Study"),
      distractors = listOf("Bookstore", "Museum", "Archive")
    ),
    WordItem(
      id = "plc_6",
      word = "Lighthouse",
      category = "Places & Travel",
      hints = listOf("Light", "Sea", "Tower"),
      distractors = listOf("Watchtower", "Windmill", "Harbor Crane")
    ),

    // Science & Tech
    WordItem(
      id = "sci_1",
      word = "Telescope",
      category = "Science & Tech",
      hints = listOf("Stars", "Night", "Look"),
      distractors = listOf("Microscope", "Binoculars", "Camera Lens")
    ),
    WordItem(
      id = "sci_2",
      word = "Black Hole",
      category = "Science & Tech",
      hints = listOf("Dark", "Space", "Deep"),
      distractors = listOf("Supernova", "Neutron Star", "Wormhole")
    ),
    WordItem(
      id = "sci_3",
      word = "Smartphone",
      category = "Science & Tech",
      hints = listOf("Pocket", "Glass", "Screen"),
      distractors = listOf("Tablet", "Smartwatch", "Laptop")
    ),
    WordItem(
      id = "sci_4",
      word = "Dinosaur",
      category = "Science & Tech",
      hints = listOf("Old", "Bones", "Big"),
      distractors = listOf("Mammoth", "Pterodactyl", "Saber-toothed Tiger")
    ),

    // Occupations
    WordItem(
      id = "occ_1",
      word = "Astronaut",
      category = "Occupations",
      hints = listOf("Space", "White", "Moon"),
      distractors = listOf("Pilot", "Deep Sea Diver", "Submarine Captain")
    ),
    WordItem(
      id = "occ_2",
      word = "Firefighter",
      category = "Occupations",
      hints = listOf("Red", "Water", "Fire"),
      distractors = listOf("Paramedic", "Police Officer", "Coast Guard")
    ),
    WordItem(
      id = "occ_3",
      word = "Chef",
      category = "Occupations",
      hints = listOf("Food", "Kitchen", "Cook"),
      distractors = listOf("Baker", "Sommelier", "Barista")
    ),
    WordItem(
      id = "occ_4",
      word = "Detective",
      category = "Occupations",
      hints = listOf("Secret", "Notes", "Search"),
      distractors = listOf("Judge", "Secret Agent", "Forensic Analyst")
    ),
    WordItem(
      id = "occ_5",
      word = "Surgeon",
      category = "Occupations",
      hints = listOf("Hospital", "Clean", "Hands"),
      distractors = listOf("Dentist", "Anesthesiologist", "Veterinarian")
    ),

    // Sports & Games
    WordItem(
      id = "spt_1",
      word = "Bowling",
      category = "Sports & Games",
      hints = listOf("Roll", "Heavy", "Ball"),
      distractors = listOf("Billiards", "Curling", "Darts")
    ),
    WordItem(
      id = "spt_2",
      word = "Chess",
      category = "Sports & Games",
      hints = listOf("Game", "Wood", "Think"),
      distractors = listOf("Checkers", "Go", "Backgammon")
    ),
    WordItem(
      id = "spt_3",
      word = "Surfing",
      category = "Sports & Games",
      hints = listOf("Water", "Waves", "Beach"),
      distractors = listOf("Skateboarding", "Snowboarding", "Wakeboarding")
    ),
    WordItem(
      id = "spt_4",
      word = "Archery",
      category = "Sports & Games",
      hints = listOf("Target", "Wood", "Fly"),
      distractors = listOf("Darts", "Fencing", "Javelin Throw")
    ),

    // Fantasy & Mystery
    WordItem(
      id = "fan_1",
      word = "Dragon",
      category = "Fantasy & Mystery",
      hints = listOf("Fire", "Wings", "Cave"),
      distractors = listOf("Phoenix", "Griffin", "Hydra")
    ),
    WordItem(
      id = "fan_2",
      word = "Vampire",
      category = "Fantasy & Mystery",
      hints = listOf("Night", "Blood", "Dark"),
      distractors = listOf("Werewolf", "Zombie", "Ghost")
    ),
    WordItem(
      id = "fan_3",
      word = "Treasure Map",
      category = "Fantasy & Mystery",
      hints = listOf("Gold", "Old", "Island"),
      distractors = listOf("Compass", "Spellbook", "Secret Blueprint")
    )
  )

  private val customWords = mutableListOf<WordItem>()
  private val random = SecureRandom()

  fun getRandomWord(selectedCategories: Set<String> = emptySet()): Pair<WordItem, String> {
    val pool = (defaultWords + customWords).filter {
      selectedCategories.isEmpty() || it.category in selectedCategories
    }
    val candidates = if (pool.isNotEmpty()) pool else defaultWords
    val item = candidates[random.nextInt(candidates.size)]
    val chosenHint = if (item.hints.isNotEmpty()) {
      item.hints[random.nextInt(item.hints.size)]
    } else "Mystery"
    return Pair(item, chosenHint)
  }

  fun addCustomWord(word: String, category: String, hint: String, distractors: List<String>) {
    val hintList = hint.split(",", ";").map { it.trim() }.filter { it.isNotEmpty() }
    customWords.add(
      WordItem(
        id = "custom_${System.currentTimeMillis()}",
        word = word.trim(),
        category = category.trim(),
        hints = if (hintList.isNotEmpty()) hintList else listOf(hint.trim()),
        distractors = distractors.map { it.trim() }.filter { it.isNotEmpty() }
      )
    )
  }

  fun getAllWords(): List<WordItem> = defaultWords + customWords

  fun getCustomWords(): List<WordItem> = customWords.toList()
}
