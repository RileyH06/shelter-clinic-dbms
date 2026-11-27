USE shelterclinic_schema;

/*DELETE FROM shelter WHERE shelter_id = ?;
DELETE FROM user WHERE user_id = ?;*/

/*SELECT * FROM shelter;
SELECT * FROM user;
SELECT * from medical_record;
SELECT * FROM medical_conditions;
*/
SELECT * from animal;
SELECT * from tag;


/*THIS FAILS WHEN SHELTER ALREADY EXISTS

INSERT INTO shelter(name, capacity, password, singleton) VALUES ("test", 120, "12345asd%", 1);*/
/*

POPULATE TABLES -----------------------------------------------------

INSERT INTO animal(name, description, age, species) 
VALUES
            ("Mittens", "Mittens previous owner passed and is now in need of a new home.", 2, "CAT"),
            ("Whiskers", "This cat was found in a walmart parking lot.", 8, "CAT"),
            ("Frisbee", "This dog loves frisbees", 12, "DOG"),
            ("Charcoal", "This dog was found in a charcoal mine", 5, "DOG"),
            ("Kibbler Nibbler", "This dog has eating issues and is being rehomed because he ate someones birth cirtificate", 0, "DOG"),
            ("Emperor Constantine", "This kitty was once an emperor of Rome. He doesn't want his capital rennamed under any circumstances.", 1, "CAT"),
            ("Mewton", "This cat was discovered as it was discovering calculus and gravity. He is a known nemesis of Mr. Peabody", 2, "CAT"),
            ("Syntax Error", "This kitty is exceptional", 10, "CAT"),
            ("John", "This dog was found lost in the woods and is in need of a new home", 2, "CAT"),
            ("Waldo", "This puppy was found in an abandoned litter", 0, "DOG");

INSERT INTO tag(tag_name)
VALUES 
    ("kitten"),
    ("puppy"),
    ("loves outdoors"),
    ("very intelligent"),
    ("needs lots of attention"),
    ("snuggler"),
    ("noisy"),
    ("calm"),
    ("aggressive"),
    ("good with children"),
    ("black"),
    ("white paws"),
    ("orange tabby"),
    ("shorthair"),
    ("black lab"),
    ("small dog"),
    ("friendly"),
    ("potty trained");

    INSERT INTO animal_tag (animal_id, tag_name)
    VALUES 
        (1, "white paws"),
        (2, "snuggler"),
        (2, "friendly"),
        (3, "loves outdoors"),
        (3, "friendly"),
        (3, "black lab"),
        (3, "potty trained"),
        (4, "black"),
        (4, "calm"),
        (4, "shorthair"),
        (5, "aggressive"),
        (6, "noisy"),
        (7, "good with children"),
        (7, "very intelligent"),
        (8, "orange tabby"),
        (9, "needs lots of attention"),
        (10, "small dog");*/