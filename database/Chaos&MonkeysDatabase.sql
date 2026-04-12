/*
Database creation
*/
DROP DATABASE IF EXISTS CHAOSMONKEYS;
CREATE DATABASE CHAOSMONKEYS;
USE CHAOSMONKEYS;

/* 
Tables creation 
*/
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
    Quality ENUM('Common', 'Rare', 'Epic', 'Legendary', 'Mythic', 'Arok'),
    PurchasePrice INT,
    SellPrice INT
);
CREATE TABLE PlayersCards (
    PlayerID INT,
    CardID INT,
    Quantity INT DEFAULT 0,
    PRIMARY KEY (PlayerID, CardID),
    FOREIGN KEY (PlayerID) REFERENCES Players(PlayerID),
    FOREIGN KEY (CardID) REFERENCES Cards(CardID)
);
CREATE TABLE ShopCards (
	PlayerID INT,
    CardID INT,
    Purchased BOOLEAN,
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

/*
Data insertion 
*/
INSERT INTO Cards (CardName, Quality, CardDescription, PurchasePrice, SellPrice) VALUES
('Royal Mandrill', 'Legendary', 'A natural leader with vibrant facial colors and a fierce gaze.',2000,150),
('Common Marmoset', 'Common', 'Tiny, elusive, and has a great love for tropical fruits.',500,10),
('Mountain Gorilla', 'Epic', 'Pure brute strength capable of uprooting entire trees.',1500,75),
('Genius Chimpanzee', 'Rare', 'Uses advanced tools and logic to solve complex puzzles.',1000,25),
('Scholar Orangutan', 'Epic', 'Possesses the ancient wisdom of the Borneo rainforests.',1500,75),
('Proboscis Primate', 'Rare', 'His large nose is his greatest pride and a social status symbol.',1000,25),
('Baboon Warrior', 'Common', 'A fierce protector of the troop across the open savannah.',500,10),
('Acrobatic Gibbon', 'Rare', 'Master of balance and momentum in the highest canopy.',1000,25),
('Lemur King', 'Legendary', 'A party animal who loves to move it and lead the dance.',2000,150),
('Thieving Capuchin', 'Common', 'Specializes in stealing sunglasses and snacks from tourists.',500,10),
('Howler Monkey', 'Common', 'His roar can be heard from several miles away.',500,10),
('Snow Macaque', 'Rare', 'Enjoys hot spring baths during the freezing Japanese winters.',1000,25),
('Jumping Squirrel Monkey', 'Common', 'Fast as the wind when moving between thin branches.',500,10),
('Bald Uakari', 'Rare', 'A bright red face indicates peak health and vitality.',1000,25),
('Forest Drill', 'Epic', 'A mysterious relative of the mandrill living in deep shadows.',1500,75),
('Barbary Macaque', 'Common', 'The only wild primate species currently living in Europe.',500,10),
('Monkey D Luffy', 'Mythic', 'Future king of the monkeys.',3000,250),
('Big-Eyed Tarsier', 'Rare', 'A nocturnal hunter that sees everything in total darkness.',1000,25),
('Spider Monkey', 'Common', 'Long limbs give him an incredible reach for distant branches.',500,10),
('Black-and-White Colobus', 'Rare', 'Natural elegance with a flowing mantle of long white fur.',1000,25),
('Saimiri Scout', 'Common', 'Small but possesses an inexhaustible supply of energy.',500,10),
('Diplomat Bonobo', 'Epic', 'Resolves all troop conflicts with hugs and peaceful vibes.',1500,75),
('Lion Tamarin', 'Rare', 'An orange mane that commands respect from all small primates.',1000,25),
('Goeldi Marmoset', 'Common', 'A small, dark primate found in the foothills of the Andes.',500,10),
('White-Fronted Capuchin', 'Common', 'Highly intelligent and adaptable to almost any environment.',500,10),
('White-Faced Saki', 'Rare', 'Looks like he is wearing a dramatic theatrical mask.',1000,25),
('Bleeding-Heart Gelada', 'Epic', 'Lives in the high-altitude grasslands of Ethiopia.',1500,75),
('Woolly Monkey', 'Common', 'Soft to the touch but incredibly strong and sturdy.',500,10),
('Pygmy Marmoset', 'Common', 'So small he can easily hide behind a single large leaf.',500,10),
('Diana Monkey', 'Rare', 'Named after the goddess of the hunt for his regal look.',1000,25),
('Jade Mandrill', 'Mythic', 'A mandrill mutated by ancient mystical energies.',3000,250),
('Albino Gorilla', 'Legendary', 'Extremely rare; he is considered a living ghost of the jungle.',2000,150),
('Cosmonaut Chimpanzee', 'Epic', 'A pioneer who has traveled far beyond the stratosphere.',1500,75),
('Carnival Monkey', 'Common', 'Plays the cymbals in exchange for a handful of peanuts.',500,10),
('Sacred Baboon', 'Rare', 'Venerated in ancient civilizations as a symbol of Thoth.',1000,25),
('Ring-Tailed Lemur', 'Common', 'Famous for his iconic black-and-white striped tail.',500,10),
('Mechanic Orangutan', 'Epic', 'Can fix any engine using only vines and raw intuition.',1500,75),
('Silvery Gibbon', 'Rare', 'His mournful morning song echoes through the misty valleys.',1000,25),
('Night Monkey', 'Common', 'Sleeps through the day and hunts under the moonlight.',500,10),
('Emperor Tamarin', 'Legendary', 'Sports a white mustache worthy of high royalty.',2000,150),
('Crab-Eating Macaque', 'Common', 'An expert at foraging for shellfish along the coast.',500,10),
('Javan Langur', 'Rare', 'Bright orange fur that stands out in the deep green wild.',1000,25),
('Preuss Monkey', 'Epic', 'A rare inhabitant of high-altitude African forests.',1500,75),
('Green Vervet', 'Common', 'Very common in the savannah; always the first to give an alarm.',500,10),
('Mountain Drill', 'Rare', 'Much more robust than his cousins living in the lowlands.',1000,25),
('Red-Backed Squirrel Monkey', 'Common', 'A tiny jumper from the central rainforests of America.',500,10),
('Black-Headed Uakari', 'Rare', 'A rare specimen from the deep Amazon river basins.',1000,25),
('Campbell Monkey', 'Common', 'Uses a complex syntax in his vocal calls to communicate.',500,10),
('Red Colobus', 'Epic', 'Often targeted by chimpanzees, he relies on speed to survive.',1500,75),
('Wolf Monkey', 'Rare', 'Distinguished by unique tufted ears and a curious nature.',1000,25),
('Anubis Baboon', 'Common', 'Named after the jackal-headed god of ancient Egypt.',500,10),
('Silverback Gorilla', 'Mythic', 'The ultimate alpha male and protector of the jungle.',3000,250),
('Rebel Chimpanzee', 'Common', 'Known for throwing mud at anyone who gets too close.',500,10),
('Mandrill Warrior', 'Rare', 'Natural war paint adorns his face for intimidation.',1000,25),
('Tapanuli Orangutan', 'Legendary', 'The rarest great ape species currently known to man.',2000,150),
('Female Proboscis', 'Common', 'Her nose is small, but her hearing is incredibly sharp.',500,10),
('Crested Gibbon', 'Epic', 'Sings complex duets with his partner every single morning.',1500,75),
('Golden-Handed Tamarin', 'Rare', 'Looks as if he is wearing a pair of bright yellow gloves.',1000,25),
('Black-Tufted Marmoset', 'Common', 'A small resident of the sprawling Brazilian forests.',500,10),
('Shaggy Saki', 'Rare', 'Looks like he is wearing a heavy wig from the 1980s.',1000,25),
('Black-Handed Spider Monkey', 'Common', 'Uses his prehensile tail as a powerful fifth limb.',500,10),
('Hamadryas Baboon', 'Rare', 'Lives in large, highly organized social harems.',1000,25),
('Formosan Macaque', 'Common', 'Endemic to the rocky islands and mountains of Taiwan.',500,10),
('Black Langur', 'Epic', 'A silent jumper that moves through the shadows like a ghost.',1500,75),
('Lowe Monkey', 'Common', 'Lives in the swampy regions of the Ivory Coast.',500,10),
('Wallace Tarsier', 'Rare', 'Named after the famous naturalist who explored these lands.',1000,25),
('White-Headed Capuchin', 'Common', 'Considered the most intelligent monkey in the Americas.',500,10),
('Yellow Woolly Monkey', 'Legendary', 'Thought to be extinct until very recently rediscovered.',2000,150),
('Yellow-Headed Marmoset', 'Rare', 'A tiny leaper with bright tufts around his ears.',1000,25),
('Hamlyn Monkey', 'Epic', 'Features a distinct white stripe across his face.',1500,75),
('Bioko Drill', 'Rare', 'An isolated subspecies from a remote volcanic island.',1000,25),
('Red Howler', 'Common', 'His fiery coloration matches his explosive vocal range.',500,10),
('Ayres Uakari', 'Legendary', 'Recently discovered in the deepest parts of the Amazon.',2000,150),
('Fat-Tailed Lemur', 'Rare', 'Stores fat in his tail to survive the lean winter months.',1000,25),
('Schouteden Chimpanzee', 'Common', 'A regional variant from the thick Congo basin.',500,10),
('Young Mandrill', 'Common', 'Has not yet developed the bright colors of an adult.',500,10),
('Lowland Gorilla', 'Epic', 'Lowland Gorilla.',1500,75),
('Sumatran Orangutan', 'Rare', 'Spends almost his entire life high up in the trees.',1000,25),
('White-Handed Gibbon', 'Common', 'Perfectly balanced swinging through the high canopy.',500,10),
('White-Lipped Tamarin', 'Rare', 'Looks as if he just finished drinking a glass of milk.',1000,25),
('De Brazza Monkey', 'Epic', 'Known as the "Bishop Monkey" for his long white beard.',1500,75),
('Guinea Baboon', 'Common', 'The smallest and most social of all baboon species.',500,10),
('Rhesus Macaque', 'Common', 'Famous for his significant contributions to medical science.',500,10),
('White-Headed Langur', 'Legendary', 'One of the most endangered primates on the entire planet.',2000,150),
('Bolivian Squirrel Monkey', 'Common', 'Lives in massive groups of up to one hundred individuals.',500,10),
('Red-Faced Saki', 'Rare', 'His face glows like a burning ember in the dark forest.',1000,25),
('Black Capuchin', 'Common', 'A robust monkey from the forests of southern America.',500,10),
('Muriqui Spider Monkey', 'Epic', 'The largest primate native to South America.',1500,75),
('Dian Tarsier', 'Rare', 'Has the largest ears relative to body size of his kind.',1000,25),
('Hoest Monkey', 'Epic', 'Unusual for a monkey, he prefers walking on the ground.',1500,75),
('Olive Baboon', 'Common', 'Greenish fur provides perfect camouflage in tall grass.',500,10),
('Japanese Macaque', 'Rare', 'His face turns bright red when he is excited or angry.',1000,25),
('Cotton-Top Tamarin', 'Legendary', 'Sports a natural white punk-rock hairstyle.',2000,150),
('Nigerian Preuss Monkey', 'Epic', 'Inhabits the mysterious cloud forests of Nigeria.',1500,75),
('Red-Eared Guenon', 'Common', 'Extremely vocal whenever he finds a good food source.',500,10),
('Vanzolini Saimiri', 'Rare', 'Lives in a very tiny, specific area of the Amazon.',1000,25),
('Van Beneden Colobus', 'Epic', 'Hides his young deep within the thickest vegetation.',1500,75),
('Black-Faced Uakari', 'Rare', 'A dark-faced ghost of the flooded Amazonian forests.',1000,25),
('Bornean Gibbon', 'Common', 'An untiring acrobat of the high tropical forest canopy.',500,10),
('Infinite Monkey', 'Mythic', 'Given enough time and a typewriter, he will write Shakespeare.',3000,250),
('Kora', 'Arok', 'I´m Kora',0,0);

/* 
Stored Procedures
*/
DROP PROCEDURE IF EXISTS RefreshShopCards;
DELIMITER $$
CREATE PROCEDURE RefreshShopCards()
BEGIN
	DECLARE playerCount INT;
    DECLARE noPlayer INT;
    DECLARE noCard INT;
    DECLARE randomNum DECIMAL(4,4);
    DECLARE newCardID INT;
    SELECT COUNT(*) INTO playerCount FROM Players; -- How many players are already in the DB
    SET noPlayer = 0;
    DELETE FROM ShopCards WHERE CardID BETWEEN 1 AND 100;
    WHILE noPlayer < playerCount DO -- Loop until reached the amount of players registered
		SET noCard = 0;
		SET noPlayer = noPlayer + 1;
        WHILE noCard < 3 DO -- Loop until reached 3 cards for each player
			SET noCard = noCard + 1;
			SELECT RAND() INTO randomNum;
            IF noCard <= 2 THEN -- Two first cards are totally random
				IF randomNum < 0.009 THEN
					SELECT CardID INTO newCardID FROM Cards WHERE Quality = "Mythic" ORDER BY RAND() LIMIT 1;
				ELSEIF randomNum < 0.04 THEN
					SELECT CardID INTO newCardID FROM Cards WHERE Quality = "Legendary" ORDER BY RAND() LIMIT 1;
				ELSEIF randomNum < 0.1 THEN
					SELECT CardID INTO newCardID FROM Cards WHERE Quality = "Epic" ORDER BY RAND() LIMIT 1;
				ELSEIF randomNum < 0.25 THEN
					SELECT CardID INTO newCardID FROM Cards WHERE Quality = "Rare" ORDER BY RAND() LIMIT 1;
				ELSE
					SELECT CardID INTO newCardID FROM Cards WHERE Quality = "Common" ORDER BY RAND() LIMIT 1;
				END IF;
			ELSE -- Last card must be RARE or higher
				IF randomNum < 0.04 THEN
					SELECT CardID INTO newCardID FROM Cards WHERE Quality = "Mythic" ORDER BY RAND() LIMIT 1;
				ELSEIF randomNum < 0.1 THEN
					SELECT CardID INTO newCardID FROM Cards WHERE Quality = "Legendary" ORDER BY RAND() LIMIT 1;
				ELSEIF randomNum < 0.25 THEN
					SELECT CardID INTO newCardID FROM Cards WHERE Quality = "Epic" ORDER BY RAND() LIMIT 1;
				ELSE
					SELECT CardID INTO newCardID FROM Cards WHERE Quality = "Rare" ORDER BY RAND() LIMIT 1;
				END IF;
            END IF;
            
			INSERT INTO ShopCards VALUES
			(noPlayer,newCardID,false);
		END WHILE;
    END WHILE;
    SELECT S.PlayerID, S.CardID, S.Purchased, C.Quality FROM ShopCards S
    JOIN Cards C ON S.CardID=C.CardID;
END$$
DELIMITER ;

/*
Event to refresh ShopCards
*/
DROP EVENT IF EXISTS Refresh;
CREATE EVENT Refresh
ON SCHEDULE EVERY 1 MINUTE
STARTS '2026-03-27 13:10:00'
DO
	CALL RefreshShopCards();

/*
AutoSell cards trigger
*/
DELIMITER $$
CREATE TRIGGER autoSell BEFORE INSERT ON PlayersCards
FOR EACH ROW
BEGIN
	DECLARE existingQuantity INT;
    SELECT Quantity into existingQuantity FROM PlayersCards
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

/*
Duplicated trigger
*/
/*
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
*/

/*
Stored Procedure to unlock Kora card
*/
DELIMITER $$
CREATE PROCEDURE unlockKora(IN id int) 
BEGIN
	IF (SELECT COUNT(*) FROM PlayersCards WHERE PlayerID = id) = 100 THEN
		INSERT INTO PlayersCards VALUES
        (id, 101, 1);
		SELECT 'Congratulations! You have completed the collection, and as a reward you have unlocked the secret card KORA.';
    END IF;
    END $$
DELIMITER ;


DELIMITER $$
CREATE FUNCTION rarestCard()
RETURNS INT
DETERMINISTIC
BEGIN
	DECLARE rarest INT;
    DECLARE currentCard INT;
    DECLARE currentQuantity INT;
    DECLARE minQuantity INT DEFAULT NULL;
    DECLARE fin bool default 0;
	DECLARE c CURSOR FOR
        SELECT CardID, SUM(Quantity) AS totalQuantity
        FROM PlayersCards
        GROUP BY CardID;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET fin = 1;
    OPEN c;
    FETCH c INTO currentCard, currentQuantity;
    WHILE fin = 0 DO
		IF minQuantity IS NULL OR currentQuantity < minQuantity THEN
			SET minQuantity = currentQuantity;
			SET rarest = currentCard;
		END IF;
        FETCH c INTO currentCard, currentQuantity;
    END WHILE;

    CLOSE c;

	RETURN rarest;
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE tradeCard(in card int , in recivPlayer int, in givePlayer int)
BEGIN
	DECLARE gQuantity INT;
    DECLARE rQuantity INT;
	BEGIN
        DECLARE EXIT HANDLER FOR NOT FOUND
        BEGIN
			SELECT 'Error: No se ha podido realizar el intercambio' as mensaje;
        END;
        SELECT Quantity INTO gQuantity 
        FROM PlayersCards 
        WHERE PlayerID = givePlayer AND CardID = card;
    END;
    SET rQuantity = (SELECT Quantity FROM PlayersCards WHERE PlayerID = recivPlayer AND  CardID = card);
	IF  gQuantity > 1 THEN
		IF rQuantity IS NOT NULL THEN
			UPDATE PlayersCards SET Quantity = rQuantity + 1
            WHERE PlayerID = recivPlayer AND  CardID = card;
		ELSE
			INSERT INTO PlayersCards VALUES
            (recivPlayer, card, 1);
        END IF;
        UPDATE PlayersCards SET Quantity = gQuantity - 1
            WHERE PlayerID = givePlayer AND  CardID = card;
	ELSE 
		SELECT 'Error: No se ha podido realizar el intercambio' as mensaje;
    END IF;
END $$
DELIMITER ;



UPDATE Cards
SET SellPrice=250
WHERE Quality='Mythic';

INSERT INTO PlayersCards (PlayerID, CardID) VALUES (1, 1);

SELECT COUNT(CardID) FROM Cards WHERE Quality='Common';
SELECT COUNT(CardID) FROM Cards WHERE Quality='Rare';
SELECT COUNT(CardID) FROM Cards WHERE Quality='Epic';
SELECT COUNT(CardID) FROM Cards WHERE Quality='Legendary';
SELECT COUNT(CardID) FROM Cards WHERE Quality='Mythic';
