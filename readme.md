## Formula
A template repository for showing how to use Scena.

### Contents
#### Common
This project contains the common files of the project, it does not contain any functional logic, but contains things access transformers and access wideners.

#### Core
This is the core project it contains the common code and the core logic of the project.
In this example this core project only initializes the common 'mod' class `Formula` and the initializers for the item the project adds.

#### Fabric
The initialization and build logic for fabric.

#### Forge
The initialization and build logic for forge.

### How to use
1. Fork this project.
2. Start modifying the `gradle.properties` file
   1. Modify your owner, maven and author data
3. Rename your project in the `settings.gradle`
4. Rename the group in the root `build.gradle`
5. Rename the packages of the core, fabric and forge projects to match the new group
6. Rename the 'mod' class `Formula` to match your new project name
7. Start building a mod.