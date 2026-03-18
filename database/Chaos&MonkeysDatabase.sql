DROP DATABASE IF EXISTS CHAOSMONKEYS;
CREATE DATABASE CHAOSMONKEYS;

USE CHAOSMONKEYS;

CREATE TABLE Players (
    PlayerID INT AUTO_INCREMENT PRIMARY KEY,
    Username VARCHAR(50) NOT NULL UNIQUE,
    PlayerPassword VARCHAR(255) NOT NULL,
    Coins INT CHECK(Coins>=0 AND Coins<=999999)
);

CREATE TABLE Decks (
    DeckID INT AUTO_INCREMENT PRIMARY KEY,
    DeckTitle VARCHAR(30) NOT NULL,
    PlayerID INT,
    DeckDescription VARCHAR(255),
	FOREIGN KEY (PlayerID) REFERENCES Players(PlayerID)
);

CREATE TABLE Cards (
    CardID INT AUTO_INCREMENT PRIMARY KEY,
    CardName VARCHAR(30) NOT NULL,
    CardDescription VARCHAR(255) ,
    Quality ENUM('Common', 'Rare', 'Epic', 'Legendary', 'Mythic', 'Arok')
);

CREATE TABLE PlayersCards (
    PlayerID INT,
    CardID INT,
    Quantity INT DEFAULT 0,
    PRIMARY KEY (PlayerID, CardID),
    FOREIGN KEY (PlayerID) REFERENCES Players(PlayerID),
    FOREIGN KEY (CardID) REFERENCES Cards(CardID)
);

CREATE TABLE DecksCards (
    DeckID INT,
    CardID INT,
    Quantity INT DEFAULT 0,
    PRIMARY KEY (DeckID, CardID),
    FOREIGN KEY (DeckID) REFERENCES Decks(DeckID),
    FOREIGN KEY (CardID) REFERENCES Cards(CardID)
);

INSERT INTO Cards (Name, Quality, Description) VALUES
('Royal Mandrill', 'Legendary', 'A natural leader with vibrant facial colors and a fierce gaze.'),
('Common Marmoset', 'Common', 'Tiny, elusive, and has a great love for tropical fruits.'),
('Mountain Gorilla', 'Epic', 'Pure brute strength capable of uprooting entire trees.'),
('Genius Chimpanzee', 'Rare', 'Uses advanced tools and logic to solve complex puzzles.'),
('Scholar Orangutan', 'Epic', 'Possesses the ancient wisdom of the Borneo rainforests.'),
('Proboscis Primate', 'Rare', 'His large nose is his greatest pride and a social status symbol.'),
('Baboon Warrior', 'Common', 'A fierce protector of the troop across the open savannah.'),
('Acrobatic Gibbon', 'Rare', 'Master of balance and momentum in the highest canopy.'),
('Lemur King', 'Legendary', 'A party animal who loves to move it and lead the dance.'),
('Thieving Capuchin', 'Common', 'Specializes in stealing sunglasses and snacks from tourists.'),
('Howler Monkey', 'Common', 'His roar can be heard from several miles away.'),
('Snow Macaque', 'Rare', 'Enjoys hot spring baths during the freezing Japanese winters.'),
('Jumping Squirrel Monkey', 'Common', 'Fast as the wind when moving between thin branches.'),
('Bald Uakari', 'Rare', 'A bright red face indicates peak health and vitality.'),
('Forest Drill', 'Epic', 'A mysterious relative of the mandrill living in deep shadows.'),
('Barbary Macaque', 'Common', 'The only wild primate species currently living in Europe.'),
('Monkey D Luffy', 'Mythic', 'Future king of the monkeys.'),
('Big-Eyed Tarsier', 'Rare', 'A nocturnal hunter that sees everything in total darkness.'),
('Spider Monkey', 'Common', 'Long limbs give him an incredible reach for distant branches.'),
('Black-and-White Colobus', 'Rare', 'Natural elegance with a flowing mantle of long white fur.'),
('Saimiri Scout', 'Common', 'Small but possesses an inexhaustible supply of energy.'),
('Diplomat Bonobo', 'Epic', 'Resolves all troop conflicts with hugs and peaceful vibes.'),
('Lion Tamarin', 'Rare', 'An orange mane that commands respect from all small primates.'),
('Goeldi Marmoset', 'Common', 'A small, dark primate found in the foothills of the Andes.'),
('White-Fronted Capuchin', 'Common', 'Highly intelligent and adaptable to almost any environment.'),
('White-Faced Saki', 'Rare', 'Looks like he is wearing a dramatic theatrical mask.'),
('Bleeding-Heart Gelada', 'Epic', 'Lives in the high-altitude grasslands of Ethiopia.'),
('Woolly Monkey', 'Common', 'Soft to the touch but incredibly strong and sturdy.'),
('Pygmy Marmoset', 'Common', 'So small he can easily hide behind a single large leaf.'),
('Diana Monkey', 'Rare', 'Named after the goddess of the hunt for his regal look.'),
('Jade Mandrill', 'Mythic', 'A mandrill mutated by ancient mystical energies.'),
('Albino Gorilla', 'Legendary', 'Extremely rare; he is considered a living ghost of the jungle.'),
('Cosmonaut Chimpanzee', 'Epic', 'A pioneer who has traveled far beyond the stratosphere.'),
('Carnival Monkey', 'Common', 'Plays the cymbals in exchange for a handful of peanuts.'),
('Sacred Baboon', 'Rare', 'Venerated in ancient civilizations as a symbol of Thoth.'),
('Ring-Tailed Lemur', 'Common', 'Famous for his iconic black-and-white striped tail.'),
('Mechanic Orangutan', 'Epic', 'Can fix any engine using only vines and raw intuition.'),
('Silvery Gibbon', 'Rare', 'His mournful morning song echoes through the misty valleys.'),
('Night Monkey', 'Common', 'Sleeps through the day and hunts under the moonlight.'),
('Emperor Tamarin', 'Legendary', 'Sports a white mustache worthy of high royalty.'),
('Crab-Eating Macaque', 'Common', 'An expert at foraging for shellfish along the coast.'),
('Javan Langur', 'Rare', 'Bright orange fur that stands out in the deep green wild.'),
('Preuss Monkey', 'Epic', 'A rare inhabitant of high-altitude African forests.'),
('Green Vervet', 'Common', 'Very common in the savannah; always the first to give an alarm.'),
('Mountain Drill', 'Rare', 'Much more robust than his cousins living in the lowlands.'),
('Red-Backed Squirrel Monkey', 'Common', 'A tiny jumper from the central rainforests of America.'),
('Black-Headed Uakari', 'Rare', 'A rare specimen from the deep Amazon river basins.'),
('Campbell Monkey', 'Common', 'Uses a complex syntax in his vocal calls to communicate.'),
('Red Colobus', 'Epic', 'Often targeted by chimpanzees, he relies on speed to survive.'),
('Wolf Monkey', 'Rare', 'Distinguished by unique tufted ears and a curious nature.'),
('Anubis Baboon', 'Common', 'Named after the jackal-headed god of ancient Egypt.'),
('Silverback Gorilla', 'Mythic', 'The ultimate alpha male and protector of the jungle.'),
('Rebel Chimpanzee', 'Common', 'Known for throwing mud at anyone who gets too close.'),
('Mandrill Warrior', 'Rare', 'Natural war paint adorns his face for intimidation.'),
('Tapanuli Orangutan', 'Legendary', 'The rarest great ape species currently known to man.'),
('Female Proboscis', 'Common', 'Her nose is small, but her hearing is incredibly sharp.'),
('Crested Gibbon', 'Epic', 'Sings complex duets with his partner every single morning.'),
('Golden-Handed Tamarin', 'Rare', 'Looks as if he is wearing a pair of bright yellow gloves.'),
('Black-Tufted Marmoset', 'Common', 'A small resident of the sprawling Brazilian forests.'),
('Shaggy Saki', 'Rare', 'Looks like he is wearing a heavy wig from the 1980s.'),
('Black-Handed Spider Monkey', 'Common', 'Uses his prehensile tail as a powerful fifth limb.'),
('Hamadryas Baboon', 'Rare', 'Lives in large, highly organized social harems.'),
('Formosan Macaque', 'Common', 'Endemic to the rocky islands and mountains of Taiwan.'),
('Black Langur', 'Epic', 'A silent jumper that moves through the shadows like a ghost.'),
('Lowe Monkey', 'Common', 'Lives in the swampy regions of the Ivory Coast.'),
('Wallace Tarsier', 'Rare', 'Named after the famous naturalist who explored these lands.'),
('White-Headed Capuchin', 'Common', 'Considered the most intelligent monkey in the Americas.'),
('Yellow Woolly Monkey', 'Legendary', 'Thought to be extinct until very recently rediscovered.'),
('Yellow-Headed Marmoset', 'Rare', 'A tiny leaper with bright tufts around his ears.'),
('Hamlyn Monkey', 'Epic', 'Features a distinct white stripe across his face.'),
('Bioko Drill', 'Rare', 'An isolated subspecies from a remote volcanic island.'),
('Red Howler', 'Common', 'His fiery coloration matches his explosive vocal range.'),
('Ayres Uakari', 'Legendary', 'Recently discovered in the deepest parts of the Amazon.'),
('Fat-Tailed Lemur', 'Rare', 'Stores fat in his tail to survive the lean winter months.'),
('Schouteden Chimpanzee', 'Common', 'A regional variant from the thick Congo basin.'),
('Young Mandrill', 'Common', 'Has not yet developed the bright colors of an adult.'),
('Lowland Gorilla', 'Epic', 'Smaller than the mountain variety but much more agile.'),
('Sumatran Orangutan', 'Rare', 'Spends almost his entire life high up in the trees.'),
('White-Handed Gibbon', 'Common', 'Perfectly balanced swinging through the high canopy.'),
('White-Lipped Tamarin', 'Rare', 'Looks as if he just finished drinking a glass of milk.'),
('De Brazza Monkey', 'Epic', 'Known as the "Bishop Monkey" for his long white beard.'),
('Guinea Baboon', 'Common', 'The smallest and most social of all baboon species.'),
('Rhesus Macaque', 'Common', 'Famous for his significant contributions to medical science.'),
('White-Headed Langur', 'Legendary', 'One of the most endangered primates on the entire planet.'),
('Bolivian Squirrel Monkey', 'Common', 'Lives in massive groups of up to one hundred individuals.'),
('Red-Faced Saki', 'Rare', 'His face glows like a burning ember in the dark forest.'),
('Black Capuchin', 'Common', 'A robust monkey from the forests of southern America.'),
('Muriqui Spider Monkey', 'Epic', 'The largest primate native to South America.'),
('Dian Tarsier', 'Rare', 'Has the largest ears relative to body size of his kind.'),
('Hoest Monkey', 'Epic', 'Unusual for a monkey, he prefers walking on the ground.'),
('Olive Baboon', 'Common', 'Greenish fur provides perfect camouflage in tall grass.'),
('Japanese Macaque', 'Rare', 'His face turns bright red when he is excited or angry.'),
('Cotton-Top Tamarin', 'Legendary', 'Sports a natural white punk-rock hairstyle.'),
('Nigerian Preuss Monkey', 'Epic', 'Inhabits the mysterious cloud forests of Nigeria.'),
('Red-Eared Guenon', 'Common', 'Extremely vocal whenever he finds a good food source.'),
('Vanzolini Saimiri', 'Rare', 'Lives in a very tiny, specific area of the Amazon.'),
('Van Beneden Colobus', 'Epic', 'Hides his young deep within the thickest vegetation.'),
('Black-Faced Uakari', 'Rare', 'A dark-faced ghost of the flooded Amazonian forests.'),
('Bornean Gibbon', 'Common', 'An untiring acrobat of the high tropical forest canopy.'),
('Infinite Monkey', 'Mythic', 'Given enough time and a typewriter, he will write Shakespeare.'),
('Kora', 'Arok', 'I´m Kora');

DELIMITER $$
CREATE TRIGGER autoSell BEFORE INSERT ON PlayerCards
FOR EACH ROW
BEGIN
	DECLARE existingQuantity INT;
    SELECT Quantity into existingQuantity FROM PlayerCards
    WHERE PlayerID = NEW.PlayerID AND CardID = NEW.CardID 
    LIMIT 1;
	IF existingQuantity IS NOT NULL THEN
		IF(existingQuantity < 5) THEN
			SET NEW.Quantity = (existingQuantity) + 1;
		ELSE
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Maximum card limit reached card sold automatically.';
		END IF;
    END IF;

END$$
DELIMITER ;

DELIMITER $$

CREATE TRIGGER autoSell BEFORE INSERT ON PlayerCards
FOR EACH ROW
BEGIN
    DECLARE existingQuantity INT DEFAULT 0;

    SELECT Quantity INTO existingQuantity 
    FROM PlayerCards
    WHERE UserID = NEW.UserID AND CardID = NEW.CardID 
    LIMIT 1;
    IF existingQuantity > 0 THEN
        IF existingQuantity < 5 THEN
            SIGNAL SQLSTATE '45000' 
            SET MESSAGE_TEXT = 'Card already exists. Increase quantity instead of inserting.';
        ELSE
            SIGNAL SQLSTATE '45000' 
            SET MESSAGE_TEXT = 'Maximum card limit reached. Card sold automatically.';
        END IF;
    END IF;
END$$

DELIMITER ;


-- Test Player Insert
INSERT INTO Player (Username, Password) VALUES ('player1', 'pass1');
INSERT INTO Player (Username, Password) VALUES ('player2', 'pass2');

INSERT INTO PlayerCards (PlayerID, CardID) VALUES (1, 1);