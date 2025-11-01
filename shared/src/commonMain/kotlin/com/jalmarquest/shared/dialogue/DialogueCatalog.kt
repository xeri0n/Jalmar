package com.jalmarquest.shared.dialogue

/**
 * Static catalog of all dialogue trees in the game.
 * Follows the established pattern from Quest/Item/Location/Dungeon catalogs.
 */
object DialogueCatalog {
    
    /**
     * All available dialogue trees in the game.
     */
    val allTrees: List<DialogueTree> = listOf(
        
        // ========== ELDER QUAIL - FIRST MEETING ==========
        
        DialogueTree(
            id = "elder_quail_greeting",
            npcId = "elder_quail",
            name = "Elder Quail - First Meeting",
            description = "Initial greeting when player first meets the village elder",
            entryNodeId = "greeting_1",
            nodes = mapOf(
                "greeting_1" to DialogueNode(
                    id = "greeting_1",
                    npcId = "elder_quail",
                    text = "Ah, a new face! Welcome to Buttonburgh, little one. I am the Elder Quail, keeper of village wisdom. You look eager to explore our humble corner of the Great Garden.",
                    choices = listOf(
                        DialogueChoice(
                            id = "eager",
                            text = "Thank you! I'm ready to prove myself.",
                            nextNodeId = "offer_tutorial"
                        ),
                        DialogueChoice(
                            id = "curious",
                            text = "What is this place exactly?",
                            nextNodeId = "lore_explanation"
                        ),
                        DialogueChoice(
                            id = "independent",
                            text = "I don't need guidance. I'll figure things out myself.",
                            nextNodeId = "refuse_help",
                            effects = DialogueEffects(
                                setFlags = mapOf("refused_elder_help" to true)
                            )
                        )
                    ),
                    flagsSet = mapOf("met_elder_quail" to true),
                    onceOnly = true
                ),
                
                "offer_tutorial" to DialogueNode(
                    id = "offer_tutorial",
                    npcId = "elder_quail",
                    text = "Wonderful spirit! Let me teach you the basics of survival. First, explore our village - familiarize yourself with the safe paths before venturing into danger.",
                    choices = listOf(
                        DialogueChoice(
                            id = "accept",
                            text = "I'm ready to learn!",
                            nextNodeId = null
                        )
                    ),
                    questActions = listOf(QuestAction.AcceptQuest("tutorial_first_steps"))
                ),
                
                "lore_explanation" to DialogueNode(
                    id = "lore_explanation",
                    npcId = "elder_quail",
                    text = "Buttonburgh is a village of button quails - tiny birds in a vast garden. To us, a puddle is a lake, a garden gnome is a fortress. We've carved out life among the giants' world. The garden provides for us, but also holds dangers: spiders, beetles, predatory insects...",
                    choices = listOf(
                        DialogueChoice(
                            id = "ready_now",
                            text = "I understand. What should I do first?",
                            nextNodeId = "offer_tutorial"
                        ),
                        DialogueChoice(
                            id = "goodbye",
                            text = "Thank you for the explanation. I'll explore on my own.",
                            nextNodeId = null,
                            effects = DialogueEffects(
                                setFlags = mapOf("heard_lore" to true)
                            )
                        )
                    )
                ),
                
                "refuse_help" to DialogueNode(
                    id = "refuse_help",
                    npcId = "elder_quail",
                    text = "*chuckles warmly* Bold spirit! I admire your independence. Just remember, even the bravest quail knows when to ask for help. If you change your mind, I'll be here.",
                    choices = listOf(
                        DialogueChoice(
                            id = "leave",
                            text = "I'll keep that in mind.",
                            nextNodeId = null
                        )
                    )
                )
            )
        ),
        
        // ========== ELDER QUAIL - QUEST TURN-IN ==========
        
        DialogueTree(
            id = "elder_quail_quest_turnin",
            npcId = "elder_quail",
            name = "Elder Quail - Quest Turn-In",
            description = "Dialogue for turning in tutorial and main quests",
            entryNodeId = "check_quests",
            nodes = mapOf(
                "check_quests" to DialogueNode(
                    id = "check_quests",
                    npcId = "elder_quail",
                    text = "Ah, you've returned! How goes your journey, young one?",
                    choices = listOf(
                        DialogueChoice(
                            id = "turn_in_first_steps",
                            text = "I've explored the village as you asked.",
                            nextNodeId = "complete_first_steps",
                            conditions = listOf(DialogueCondition.QuestCompleted("tutorial_first_steps")),
                            hidden = true
                        ),
                        DialogueChoice(
                            id = "turn_in_gnome",
                            text = "I investigated the Garden Gnome threat.",
                            nextNodeId = "complete_gnome_quest",
                            conditions = listOf(DialogueCondition.QuestCompleted("main_gnome_threat")),
                            hidden = true
                        ),
                        DialogueChoice(
                            id = "just_checking_in",
                            text = "Just checking in. I'll continue my tasks.",
                            nextNodeId = null
                        )
                    )
                ),
                
                "complete_first_steps" to DialogueNode(
                    id = "complete_first_steps",
                    npcId = "elder_quail",
                    text = "Excellent! You've taken your first steps into our world. Now, you're ready for greater challenges. There are insects to defeat, items to gather, and mysteries to uncover. The garden awaits!",
                    choices = listOf(
                        DialogueChoice(
                            id = "thank_you",
                            text = "Thank you for the guidance!",
                            nextNodeId = null,
                            effects = DialogueEffects(
                                setFlags = mapOf("completed_tutorial" to true)
                            )
                        )
                    ),
                    questActions = listOf(QuestAction.TurnInQuest("tutorial_first_steps"))
                ),
                
                "complete_gnome_quest" to DialogueNode(
                    id = "complete_gnome_quest",
                    npcId = "elder_quail",
                    text = "You defeated the beetles at the Garden Gnome? Remarkable! Those creatures have terrorized our scouts for weeks. You've proven yourself a true warrior of Buttonburgh. This village owes you a debt of gratitude.",
                    choices = listOf(
                        DialogueChoice(
                            id = "humble",
                            text = "I'm just doing what's right.",
                            nextNodeId = null,
                            effects = DialogueEffects(
                                relationshipChange = 10
                            )
                        ),
                        DialogueChoice(
                            id = "boastful",
                            text = "Of course I succeeded. I'm the best.",
                            nextNodeId = null,
                            effects = DialogueEffects(
                                relationshipChange = -5
                            )
                        )
                    ),
                    questActions = listOf(QuestAction.TurnInQuest("main_gnome_threat"))
                )
            )
        ),
        
        // ========== CRAFTSMAN QUAIL - GREETING ==========
        
        DialogueTree(
            id = "craftsman_greeting",
            npcId = "craftsman_quail",
            name = "Craftsman Quail - First Meeting",
            description = "Introduction to the village craftsman",
            entryNodeId = "intro",
            nodes = mapOf(
                "intro" to DialogueNode(
                    id = "intro",
                    npcId = "craftsman_quail",
                    text = "Welcome to The Quailsmith! I'm the finest crafter in Buttonburgh - though I'm also the only crafter, so take that as you will. *chuckles* Need a twig sharpened into a spear? An acorn cap fashioned into a helmet? You've come to the right quail!",
                    choices = listOf(
                        DialogueChoice(
                            id = "ask_craft",
                            text = "Can you teach me to craft?",
                            nextNodeId = "explain_crafting"
                        ),
                        DialogueChoice(
                            id = "ask_materials",
                            text = "What materials do you need?",
                            nextNodeId = "materials_needed"
                        ),
                        DialogueChoice(
                            id = "goodbye",
                            text = "Just browsing. I'll be back.",
                            nextNodeId = null
                        )
                    ),
                    flagsSet = mapOf("met_craftsman" to true),
                    onceOnly = true
                ),
                
                "explain_crafting" to DialogueNode(
                    id = "explain_crafting",
                    npcId = "craftsman_quail",
                    text = "Crafting is simple! Gather materials from the garden - twigs, acorn caps, beetle shells, spider silk. Bring them to me, and I'll teach you how to fashion them into proper equipment. A twig becomes a spear, an acorn cap becomes armor. Magic? No. Skill? Absolutely!",
                    choices = listOf(
                        DialogueChoice(
                            id = "learn_weapon",
                            text = "Teach me to craft a weapon!",
                            nextNodeId = null
                        )
                    ),
                    questActions = listOf(QuestAction.AcceptQuest("craft_first_weapon"))
                ),
                
                "materials_needed" to DialogueNode(
                    id = "materials_needed",
                    npcId = "craftsman_quail",
                    text = "Always on the lookout for spider silk - it's rare and makes excellent lightweight armor. Bring me 5 strands, and I'll unlock a special recipe for you. Otherwise, twigs and beetle shells are always useful!",
                    choices = listOf(
                        DialogueChoice(
                            id = "accept_silk_quest",
                            text = "I'll gather spider silk for you.",
                            nextNodeId = null,
                            conditions = listOf(DialogueCondition.QuestNotStarted("side_spider_silk"))
                        ),
                        DialogueChoice(
                            id = "maybe_later",
                            text = "I'll keep that in mind.",
                            nextNodeId = null
                        )
                    ),
                    questActions = listOf(QuestAction.AcceptQuest("side_spider_silk"))
                )
            )
        ),
        
        // ========== YOUNG QUAIL - LOST FEATHER ==========
        
        DialogueTree(
            id = "young_quail_lost_feather",
            npcId = "young_quail",
            name = "Young Quail - Lost Feather Quest",
            description = "A young quail has lost their favorite feather",
            entryNodeId = "crying",
            nodes = mapOf(
                "crying" to DialogueNode(
                    id = "crying",
                    npcId = "young_quail",
                    text = "*sniffles* My favorite feather... I lost it in the meadow. It's shiny and has a little red tip. Mama says I shouldn't go out there alone because of the grasshoppers...",
                    choices = listOf(
                        DialogueChoice(
                            id = "offer_help",
                            text = "Don't cry! I'll find your feather.",
                            nextNodeId = "thank_you"
                        ),
                        DialogueChoice(
                            id = "too_busy",
                            text = "I'm sorry, I'm busy right now.",
                            nextNodeId = "sad_response"
                        )
                    ),
                    flagsSet = mapOf("met_young_quail" to true),
                    onceOnly = true
                ),
                
                "thank_you" to DialogueNode(
                    id = "thank_you",
                    npcId = "young_quail",
                    text = "*eyes light up* Really?! You're the best! Please be careful - there are big scary grasshoppers out there!",
                    choices = listOf(
                        DialogueChoice(
                            id = "accept",
                            text = "I'll be careful. I'll find it!",
                            nextNodeId = null
                        )
                    ),
                    questActions = listOf(QuestAction.AcceptQuest("side_lost_feather"))
                ),
                
                "sad_response" to DialogueNode(
                    id = "sad_response",
                    npcId = "young_quail",
                    text = "*looks down sadly* Oh... okay. I understand. Everyone's so busy...",
                    choices = listOf(
                        DialogueChoice(
                            id = "leave",
                            text = "...",
                            nextNodeId = null
                        )
                    )
                )
            )
        ),
        
        // ========== YOUNG QUAIL - FEATHER RETURN ==========
        
        DialogueTree(
            id = "young_quail_return_feather",
            npcId = "young_quail",
            name = "Young Quail - Feather Return",
            description = "Returning the lost feather",
            entryNodeId = "check_feather",
            nodes = mapOf(
                "check_feather" to DialogueNode(
                    id = "check_feather",
                    npcId = "young_quail",
                    text = "Did you find my feather?",
                    choices = listOf(
                        DialogueChoice(
                            id = "return_feather",
                            text = "Yes! Here it is.",
                            nextNodeId = "reward_reaction",
                            conditions = listOf(
                                DialogueCondition.QuestCompleted("side_lost_feather"),
                                DialogueCondition.HasItem("lost_feather", 1)
                            )
                        ),
                        DialogueChoice(
                            id = "still_looking",
                            text = "Still looking. Don't worry!",
                            nextNodeId = null,
                            conditions = listOf(DialogueCondition.QuestActive("side_lost_feather"))
                        )
                    )
                ),
                
                "reward_reaction" to DialogueNode(
                    id = "reward_reaction",
                    npcId = "young_quail",
                    text = "*jumps with joy* YOU FOUND IT! Thank you thank you thank you! Here, take this! Mama gave me some shiny seeds and a glimmer shard to give to whoever helped me!",
                    choices = listOf(
                        DialogueChoice(
                            id = "accept_reward",
                            text = "You're very welcome!",
                            nextNodeId = null,
                            effects = DialogueEffects(
                                setFlags = mapOf("helped_young_quail" to true)
                            )
                        )
                    ),
                    questActions = listOf(QuestAction.TurnInQuest("side_lost_feather"))
                )
            )
        ),
        
        // ========== COMPANION DIALOGUE TREES ==========
        
        // Pip - The Young Quail
        DialogueTree(
            id = "dialogue_pip",
            npcId = "pip_young_quail",
            name = "Pip Conversation",
            description = "Chat with Pip the young quail companion",
            entryNodeId = "pip_greeting",
            nodes = mapOf(
                "pip_greeting" to DialogueNode(
                    id = "pip_greeting",
                    npcId = "pip_young_quail",
                    text = "Hey! What's up? Ready for more adventure?",
                    choices = listOf(
                        DialogueChoice(
                            id = "chat",
                            text = "How are you feeling?",
                            nextNodeId = "pip_feelings"
                        ),
                        DialogueChoice(
                            id = "advice",
                            text = "Any advice for our journey?",
                            nextNodeId = "pip_advice"
                        ),
                        DialogueChoice(
                            id = "goodbye",
                            text = "Just checking in. Let's keep moving.",
                            nextNodeId = null
                        )
                    )
                ),
                "pip_feelings" to DialogueNode(
                    id = "pip_feelings",
                    npcId = "pip_young_quail",
                    text = "I'm doing great! Traveling with you is the best thing that ever happened to me. I'm learning so much!",
                    choices = listOf(
                        DialogueChoice(
                            id = "continue",
                            text = "Glad to have you along.",
                            nextNodeId = null
                        )
                    )
                ),
                "pip_advice" to DialogueNode(
                    id = "pip_advice",
                    npcId = "pip_young_quail",
                    text = "Well, I'm still pretty new to this... but I think the most important thing is to never give up! Even when things look scary, we can get through it together!",
                    choices = listOf(
                        DialogueChoice(
                            id = "thanks",
                            text = "Good thinking, Pip.",
                            nextNodeId = null
                        )
                    )
                )
            )
        ),
        
        // Grumble Forgepaw - The Craftsman Mole
        DialogueTree(
            id = "dialogue_grumble",
            npcId = "grumble_forgepaw",
            name = "Grumble Conversation",
            description = "Chat with Grumble the craftsman mole",
            entryNodeId = "grumble_greeting",
            nodes = mapOf(
                "grumble_greeting" to DialogueNode(
                    id = "grumble_greeting",
                    npcId = "grumble_forgepaw",
                    text = "*grunts* What is it? I'm busy. Make it quick.",
                    choices = listOf(
                        DialogueChoice(
                            id = "craft",
                            text = "Can you tell me about your craft?",
                            nextNodeId = "grumble_craft"
                        ),
                        DialogueChoice(
                            id = "mood",
                            text = "You seem grumpy today.",
                            nextNodeId = "grumble_mood"
                        ),
                        DialogueChoice(
                            id = "goodbye",
                            text = "Never mind, I'll leave you to it.",
                            nextNodeId = null
                        )
                    )
                ),
                "grumble_craft" to DialogueNode(
                    id = "grumble_craft",
                    npcId = "grumble_forgepaw",
                    text = "*eyes light up* Ah, now that's a worthy question. Quality craftsmanship takes patience, precision, and the right materials. Every piece must be perfect.",
                    choices = listOf(
                        DialogueChoice(
                            id = "appreciate",
                            text = "Your work is impressive.",
                            nextNodeId = null
                        )
                    )
                ),
                "grumble_mood" to DialogueNode(
                    id = "grumble_mood",
                    npcId = "grumble_forgepaw",
                    text = "*snorts* I'm not grumpy. I'm focused. There's a difference. Distractions lead to mistakes, and I don't make mistakes.",
                    choices = listOf(
                        DialogueChoice(
                            id = "understood",
                            text = "I understand. I'll let you work.",
                            nextNodeId = null
                        )
                    )
                )
            )
        ),
        
        // Whisker - The Scout Mouse
        DialogueTree(
            id = "dialogue_whisker",
            npcId = "whisker_scout",
            name = "Whisker Conversation",
            description = "Chat with Whisker the scout mouse",
            entryNodeId = "whisker_greeting",
            nodes = mapOf(
                "whisker_greeting" to DialogueNode(
                    id = "whisker_greeting",
                    npcId = "whisker_scout",
                    text = "*whiskers twitch* Something on your mind? I've been scouting ahead - the path looks clear for now.",
                    choices = listOf(
                        DialogueChoice(
                            id = "scout",
                            text = "What did you see ahead?",
                            nextNodeId = "whisker_scout_report"
                        ),
                        DialogueChoice(
                            id = "background",
                            text = "How did you become a scout?",
                            nextNodeId = "whisker_background"
                        ),
                        DialogueChoice(
                            id = "goodbye",
                            text = "Thanks for watching our backs.",
                            nextNodeId = null
                        )
                    )
                ),
                "whisker_scout_report" to DialogueNode(
                    id = "whisker_scout_report",
                    npcId = "whisker_scout",
                    text = "The usual hazards - some beetles in the tall grass, a spider web near the old stump. Nothing we can't handle if we're careful.",
                    choices = listOf(
                        DialogueChoice(
                            id = "thanks",
                            text = "Good to know. Stay alert.",
                            nextNodeId = null
                        )
                    )
                ),
                "whisker_background" to DialogueNode(
                    id = "whisker_background",
                    npcId = "whisker_scout",
                    text = "*sits on haunches* Growing up in the hedge maze taught me to move quietly and notice everything. When you're small, awareness is survival.",
                    choices = listOf(
                        DialogueChoice(
                            id = "respect",
                            text = "Your skills have saved us more than once.",
                            nextNodeId = null
                        )
                    )
                )
            )
        ),
        
        // Ember - The Fire Beetle
        DialogueTree(
            id = "dialogue_ember",
            npcId = "ember_beetle",
            name = "Ember Conversation",
            description = "Chat with Ember the fire beetle",
            entryNodeId = "ember_greeting",
            nodes = mapOf(
                "ember_greeting" to DialogueNode(
                    id = "ember_greeting",
                    npcId = "ember_beetle",
                    text = "*carapace glows warmly* Greetings, friend. The fire within burns bright today.",
                    choices = listOf(
                        DialogueChoice(
                            id = "fire",
                            text = "Tell me about your fire.",
                            nextNodeId = "ember_fire"
                        ),
                        DialogueChoice(
                            id = "philosophy",
                            text = "You seem thoughtful.",
                            nextNodeId = "ember_philosophy"
                        ),
                        DialogueChoice(
                            id = "goodbye",
                            text = "Stay warm, Ember.",
                            nextNodeId = null
                        )
                    )
                ),
                "ember_fire" to DialogueNode(
                    id = "ember_fire",
                    npcId = "ember_beetle",
                    text = "The bioluminescence is part of who I am - both light in darkness and warmth in cold. It can comfort or burn, depending on need.",
                    choices = listOf(
                        DialogueChoice(
                            id = "beautiful",
                            text = "It's beautiful.",
                            nextNodeId = null
                        )
                    )
                ),
                "ember_philosophy" to DialogueNode(
                    id = "ember_philosophy",
                    npcId = "ember_beetle",
                    text = "Life is like fire - it grows, consumes, and eventually fades. What matters is what light we bring while we burn.",
                    choices = listOf(
                        DialogueChoice(
                            id = "wise",
                            text = "Wise words.",
                            nextNodeId = null
                        )
                    )
                )
            )
        ),
        
        // Skitter - The Grasshopper
        DialogueTree(
            id = "dialogue_skitter",
            npcId = "skitter_hopper",
            name = "Skitter Conversation",
            description = "Chat with Skitter the grasshopper",
            entryNodeId = "skitter_greeting",
            nodes = mapOf(
                "skitter_greeting" to DialogueNode(
                    id = "skitter_greeting",
                    npcId = "skitter_hopper",
                    text = "*bounces excitedly* Hey hey hey! Want to race? Or explore? Or find something shiny? So many possibilities!",
                    choices = listOf(
                        DialogueChoice(
                            id = "energy",
                            text = "Where do you get all this energy?",
                            nextNodeId = "skitter_energy"
                        ),
                        DialogueChoice(
                            id = "focus",
                            text = "Can you focus for a moment?",
                            nextNodeId = "skitter_focus"
                        ),
                        DialogueChoice(
                            id = "goodbye",
                            text = "Maybe later, Skitter.",
                            nextNodeId = null
                        )
                    )
                ),
                "skitter_energy" to DialogueNode(
                    id = "skitter_energy",
                    npcId = "skitter_hopper",
                    text = "*leaps in a circle* I don't know! The world is just so interesting! Every blade of grass is different, every stone unique!",
                    choices = listOf(
                        DialogueChoice(
                            id = "admire",
                            text = "Your enthusiasm is infectious.",
                            nextNodeId = null
                        )
                    )
                ),
                "skitter_focus" to DialogueNode(
                    id = "skitter_focus",
                    npcId = "skitter_hopper",
                    text = "*stops bouncing* ...Okay! What did you want to talk about? I'm listening! Really!",
                    choices = listOf(
                        DialogueChoice(
                            id = "good",
                            text = "Never mind. Keep being you.",
                            nextNodeId = null
                        )
                    )
                )
            )
        ),
        
        // Swoop - The Dragonfly
        DialogueTree(
            id = "dialogue_swoop",
            npcId = "swoop_dragonfly",
            name = "Swoop Conversation",
            description = "Chat with Swoop the dragonfly",
            entryNodeId = "swoop_greeting",
            nodes = mapOf(
                "swoop_greeting" to DialogueNode(
                    id = "swoop_greeting",
                    npcId = "swoop_dragonfly",
                    text = "*hovers at eye level* You called? I've been keeping watch from above.",
                    choices = listOf(
                        DialogueChoice(
                            id = "aerial",
                            text = "What can you see from up there?",
                            nextNodeId = "swoop_aerial"
                        ),
                        DialogueChoice(
                            id = "flight",
                            text = "Your flying is incredible.",
                            nextNodeId = "swoop_flight"
                        ),
                        DialogueChoice(
                            id = "goodbye",
                            text = "Keep up the good work.",
                            nextNodeId = null
                        )
                    )
                ),
                "swoop_aerial" to DialogueNode(
                    id = "swoop_aerial",
                    npcId = "swoop_dragonfly",
                    text = "*wings shimmer* The garden stretches far beyond what we can walk in a day. I see paths we haven't taken, dangers to avoid, opportunities to seize.",
                    choices = listOf(
                        DialogueChoice(
                            id = "valuable",
                            text = "Your perspective is valuable.",
                            nextNodeId = null
                        )
                    )
                ),
                "swoop_flight" to DialogueNode(
                    id = "swoop_flight",
                    npcId = "swoop_dragonfly",
                    text = "*performs a graceful loop* Flight is freedom. To see the world from above changes everything - problems seem smaller, solutions clearer.",
                    choices = listOf(
                        DialogueChoice(
                            id = "envy",
                            text = "I wish I could fly.",
                            nextNodeId = null
                        )
                    )
                )
            )
        ),
        
        // Shimmer - The Butterfly
        DialogueTree(
            id = "dialogue_shimmer",
            npcId = "shimmer_butterfly",
            name = "Shimmer Conversation",
            description = "Chat with Shimmer the butterfly",
            entryNodeId = "shimmer_greeting",
            nodes = mapOf(
                "shimmer_greeting" to DialogueNode(
                    id = "shimmer_greeting",
                    npcId = "shimmer_butterfly",
                    text = "*wings flutter gently* Peace be with you, friend. What weighs on your heart?",
                    choices = listOf(
                        DialogueChoice(
                            id = "transformation",
                            text = "Tell me about your transformation.",
                            nextNodeId = "shimmer_transformation"
                        ),
                        DialogueChoice(
                            id = "healing",
                            text = "How do you heal others?",
                            nextNodeId = "shimmer_healing"
                        ),
                        DialogueChoice(
                            id = "goodbye",
                            text = "Just wanted to say hello.",
                            nextNodeId = null
                        )
                    )
                ),
                "shimmer_transformation" to DialogueNode(
                    id = "shimmer_transformation",
                    npcId = "shimmer_butterfly",
                    text = "*settles on a leaf* I was not always as you see me. The chrysalis taught me patience, the emergence taught me rebirth. We all transform, in our own ways.",
                    choices = listOf(
                        DialogueChoice(
                            id = "profound",
                            text = "That's beautiful.",
                            nextNodeId = null
                        )
                    )
                ),
                "shimmer_healing" to DialogueNode(
                    id = "shimmer_healing",
                    npcId = "shimmer_butterfly",
                    text = "*wings glow softly* The pollen I carry has restorative properties. But true healing comes from within - I merely help the body remember how to mend itself.",
                    choices = listOf(
                        DialogueChoice(
                            id = "grateful",
                            text = "We're lucky to have you.",
                            nextNodeId = null
                        )
                    )
                )
            )
        ),
        
        // Thorn - The Hedgehog
        DialogueTree(
            id = "dialogue_thorn",
            npcId = "thorn_hedgehog",
            name = "Thorn Conversation",
            description = "Chat with Thorn the hedgehog",
            entryNodeId = "thorn_greeting",
            nodes = mapOf(
                "thorn_greeting" to DialogueNode(
                    id = "thorn_greeting",
                    npcId = "thorn_hedgehog",
                    text = "*uncurls slightly* You need something? I'm here to protect, not chat.",
                    choices = listOf(
                        DialogueChoice(
                            id = "defense",
                            text = "Your defensive skills are impressive.",
                            nextNodeId = "thorn_defense"
                        ),
                        DialogueChoice(
                            id = "soften",
                            text = "You can relax around me.",
                            nextNodeId = "thorn_soften"
                        ),
                        DialogueChoice(
                            id = "goodbye",
                            text = "Just checking in.",
                            nextNodeId = null
                        )
                    )
                ),
                "thorn_defense" to DialogueNode(
                    id = "thorn_defense",
                    npcId = "thorn_hedgehog",
                    text = "*spines bristle proudly* These quills have saved my life more times than I can count. A good defense is often the best offense.",
                    choices = listOf(
                        DialogueChoice(
                            id = "agree",
                            text = "Can't argue with that.",
                            nextNodeId = null
                        )
                    )
                ),
                "thorn_soften" to DialogueNode(
                    id = "thorn_soften",
                    npcId = "thorn_hedgehog",
                    text = "*spines lower slightly* ...Maybe. But old habits die hard. I've protected myself for so long, trusting doesn't come easy.",
                    choices = listOf(
                        DialogueChoice(
                            id = "patient",
                            text = "I'll earn that trust.",
                            nextNodeId = null
                        )
                    )
                )
            )
        ),
        
        // Clover - The Ladybug
        DialogueTree(
            id = "dialogue_clover",
            npcId = "clover_ladybug",
            name = "Clover Conversation",
            description = "Chat with Clover the ladybug",
            entryNodeId = "clover_greeting",
            nodes = mapOf(
                "clover_greeting" to DialogueNode(
                    id = "clover_greeting",
                    npcId = "clover_ladybug",
                    text = "*cheerfully* Oh hello! Isn't it a lovely day? Even the dangerous bits are exciting when you think about it!",
                    choices = listOf(
                        DialogueChoice(
                            id = "optimism",
                            text = "How do you stay so cheerful?",
                            nextNodeId = "clover_optimism"
                        ),
                        DialogueChoice(
                            id = "luck",
                            text = "Do you really bring good luck?",
                            nextNodeId = "clover_luck"
                        ),
                        DialogueChoice(
                            id = "goodbye",
                            text = "Your positivity is contagious.",
                            nextNodeId = null
                        )
                    )
                ),
                "clover_optimism" to DialogueNode(
                    id = "clover_optimism",
                    npcId = "clover_ladybug",
                    text = "*spots on carapace seem to shine* Life is too short to be grumpy! Sure, there are scary things out there, but there are also beautiful flowers, kind friends, and endless adventures!",
                    choices = listOf(
                        DialogueChoice(
                            id = "inspired",
                            text = "You're absolutely right.",
                            nextNodeId = null
                        )
                    )
                ),
                "clover_luck" to DialogueNode(
                    id = "clover_luck",
                    npcId = "clover_ladybug",
                    text = "*giggles* Who knows? Maybe it's luck, maybe it's just paying attention and being ready when opportunities appear. Either way, I'm happy to share!",
                    choices = listOf(
                        DialogueChoice(
                            id = "thanks",
                            text = "I'll take all the luck I can get!",
                            nextNodeId = null
                        )
                    )
                )
            )
        ),
        
        // Rumble - The Dung Beetle
        DialogueTree(
            id = "dialogue_rumble",
            npcId = "rumble_beetle",
            name = "Rumble Conversation",
            description = "Chat with Rumble the dung beetle",
            entryNodeId = "rumble_greeting",
            nodes = mapOf(
                "rumble_greeting" to DialogueNode(
                    id = "rumble_greeting",
                    npcId = "rumble_beetle",
                    text = "*deep rumbling voice* You want to talk? Alright. What's on your mind?",
                    choices = listOf(
                        DialogueChoice(
                            id = "strength",
                            text = "Your strength is incredible.",
                            nextNodeId = "rumble_strength"
                        ),
                        DialogueChoice(
                            id = "work",
                            text = "Do you miss your work?",
                            nextNodeId = "rumble_work"
                        ),
                        DialogueChoice(
                            id = "goodbye",
                            text = "Just wanted to check in.",
                            nextNodeId = null
                        )
                    )
                ),
                "rumble_strength" to DialogueNode(
                    id = "rumble_strength",
                    npcId = "rumble_beetle",
                    text = "*flexes mandibles* Comes from years of rolling. Build strength one push at a time. Same as life - tackle big problems bit by bit.",
                    choices = listOf(
                        DialogueChoice(
                            id = "wisdom",
                            text = "That's good advice.",
                            nextNodeId = null
                        )
                    )
                ),
                "rumble_work" to DialogueNode(
                    id = "rumble_work",
                    npcId = "rumble_beetle",
                    text = "*thoughtful pause* Sometimes. There was satisfaction in the work. But this... traveling, fighting alongside you... it has purpose too. Different kind of rolling.",
                    choices = listOf(
                        DialogueChoice(
                            id = "glad",
                            text = "Glad you're here with us.",
                            nextNodeId = null
                        )
                    )
                )
            )
        )
    )
    
    /**
     * Retrieves a dialogue tree by ID.
     */
    fun getTree(treeId: String): DialogueTree? {
        return allTrees.find { it.id == treeId }
    }
    
    /**
     * Retrieves all dialogue trees for a specific NPC.
     */
    fun getTreesForNPC(npcId: String): List<DialogueTree> {
        return allTrees.filter { it.npcId == npcId }
    }
    
    /**
     * Returns total tree count (for validation/stats).
     */
    fun getTotalTreeCount(): Int = allTrees.size
    
    /**
     * Validates all dialogue trees.
     * Throws IllegalStateException if validation fails.
     */
    fun validateCatalog() {
        // Check for unique IDs
        val ids = allTrees.map { it.id }
        val duplicates = ids.groupingBy { it }.eachCount().filter { it.value > 1 }
        
        if (duplicates.isNotEmpty()) {
            throw IllegalStateException("Duplicate dialogue tree IDs found: ${duplicates.keys}")
        }
        
        // Validate each tree
        allTrees.forEach { tree ->
            try {
                tree.validate()
            } catch (e: IllegalStateException) {
                throw IllegalStateException("Validation failed for tree '${tree.id}': ${e.message}", e)
            }
        }
    }
}
