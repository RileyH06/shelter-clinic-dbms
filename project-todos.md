**Features and Queries:** 

\*If user is logged in, enable admit/adopt buttons(this might just be a UI/java thing)



**Account Creation/login**
\*make database queries with form info
\*add back to home page buttons



**Admit Animal**

*(Some of the attributes/entities below are optional and can be omitted)*

\* Insert into Animal(name, description, species, age) 

FOR PROVIDING ANY MEDICAL HISTORY

\* Insert into Medical\_Record(animalID, spayedNeutered, isVaccinated)

\* For each medical condition listed --> Insert into medical\_conditions(animalID, medicalCondition)

FOR ADDING TAGS AT ADMISSION

\* Select all tagNames to display

\* Insert into Tag(tagName) if tagname NOT EXISTS

\* For each tagName added --> Insert into Animal\_Tags(animalID, tagName)



**Search for animals**

*(users can search with any combination of filters)*

\* Select all from Animal

\* Select all animals with tagnames listed (use OR for partial matches)

\* Select all from Animal with specified species, age, durationInShelter

\* Select all from animal with specified medical history   isVaccinated, is fixed, # of medical conditions



**Adopt Animal**

*(user must be logged in for this button to be enabled)*

\* Delete from Animal where id = id of animal chosen by user



**Update Animal**

*(only shelter can do this)*

\* Update Animal name or description using given id

UPDATING TAGS

\* Use same queries as animal admission(inserting into Tag and Animal\_Tag)

\* Delete from animal\_tag(animalID, tagName) using given ids

\* Delete from Tag (to remove tag as an option altogether) using given tagName

UPDATING MEDICAL RECORD

\* Update isVaccinated

\* Update isFixed

\* Use same queries as animal admission to add new conditions







**detailed feature descriptions from report 1 for clarity**

**Account creation**

The user fills out a form with their email and a password to create an account. This information is stored in the database as a new user instance with the email address as a primary key. Once the account is made, the user is automatically logged in and sent to the home page.

**Login to an Account**

The user enters their username and password into a form. These credentials are checked with the user instances in the database, and the user will be logged in if the credentials match an existing user within the system.

**Admit Animal**

The logged in user fills out a form with all the information of the animal they wish to admit. The required information to admit is a description of the animal s known background, and a species type. If the information entered matches all the database constraints, a new animal instance is created with the information entered by the user. The optional fields not filled out will be stored as nullish values in the database. When complete, the  admits  relationship variable for the date is set to the current date.

**Creating and Adding Tag Descriptions**

During animal admission or updating workflows, the user or shelter may wish to provide tag descriptors for an animal. This could be related to appearance, personality, or any other descriptors the animal may have. Before creating a new tag, the user can first browse all the existing tags displayed as a list from the database, and select any they find applicable. If a tag is selected from the list, the database creates a new Animal\_Tag entity using the id of the current animal instance, and the tag name chosen. Similarly, if the user wants to create a new tag not listed, they will enter the desired tag name into a form. This will create a new tag entity in the database, which will then be used to create an Animal\_Tag instance to link that animal to that tag. 

**Create Medical Record**

If a user or shelter wants to create a medical record for an animal, they will be prompted to fill out a form. This form will ask for 2 mandatory attributes   the vaccination status, and status of whether the animal is spayed/neutered. Optionally, they can provide multiple short descriptions of each known condition. Once the form is submitted with valid data, the database will create a medical record instance for that animal, using the animal ID as a foreign key. 

**Adopt Animal**

When a user finds an animal they wish to adopt, they can select adopt on the user interface. This will trigger the user interface to leave a message indicating that the adoption is complete, and the instance of the adopted animal will be deleted from the database. 

**Search for Animals**

From the main user dashboard, all the animals in the database will be displayed. Optionally, the user can fill out a filters form with desired traits like species, age, duration of stay, and specific tags. The database will then be queried for all the animals matching these attributes and display them to the dashboard.

Searching Using Tags

If the user decides to use tags in a filtered search, the database will query for all the Animal\_Tag instances with those tag names, and use their associated animal IDs to locate which animals to display for that tag.

**Update Animal Info**

To update information, a shelter must enter the desired animal ID number. This will be checked against the database to see if such an animal exists. If it does, the shelter will be prompted whether they would like to update the medical record, name, description, or tags. For each scenario, if the shelter enters valid information, the database will update that attribute for that animal instance. For updating tags, the tag creation workflow will be used. For deleting them, the deleting tags workflow is used.

**Deleting Tags on an Animal**

If the shelter chooses to delete the tags of an animal during the update workflow, they will be prompted to select which tags to delete. The database will then delete the Animal\_Tag instance with the given animal id and tag name. 

**Update Medical Record**

After choosing an animal to update that the database has verified exists, the shelter may choose to update its medical record. In this scenario, the database will locate the record using the animal id provided. If no record exists, the shelter will follow the create record workflow. If the record exists, the shelter will be allowed to add to the conditions list, change the spayed/neutered status, or vaccination status. 





