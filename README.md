# Customwidgets

Android-Library which contains several Helper-Classes and Widgets like a calendar and a Swipe-Refresh-List.

## Content

### Widgets
  1. SwipeRefreshDeleteList<br/>
    <img src="https://raw.githubusercontent.com/domjos1994/Customwidgets/master/images/swiperefreshdeletelist.png" title="SwipeRefreshDeleteList" width="400"/>
      - Refresh
      - Swipe to delete
      - Select multiple items
      - add Buttons to Footer-Menu
      - Custom Design
     
  2. Calendar<br/>
     <img src="https://raw.githubusercontent.com/domjos1994/Customwidgets/master/images/calendar.png" title="SwipeRefreshDeleteList" width="400"/>
     - Add Groups
     - Add Events
     - Month and Day-View
  3. Expandable Texview<br/>
      <img src="https://raw.githubusercontent.com/domjos1994/Customwidgets/master/images/expandabletextview.png" title="SwipeRefreshDeleteList" width="400"/>
      - Click to expand
      
### Utils
  1. Validator-Class (Validate Textfields)
  2. ConvertHelper (Helper-Functions to convert Data)
  3. Crypto (Encrypt, Decrypt Data)
  4. MessageHelper (Toasts, Notifications, ...)
  
### Model
  1. Custom Model for Activities

## How to include it
1. Create Properties-File with your Github-Login-Data and save it in Project-Root
2. Get Properties in Gradle Build-File
   ```
    def githubProperties = new Properties()
    githubProperties.load(new FileInputStream(new File(projectDir, "<Relative Path to Properties-File>")))
   ```
3. Add Repository to Gradle Build-File
   ```
   ...
   maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/domjos1994/Customwidgets")
        credentials {
            username = githubProperties['gpr.usr']
            password = githubProperties['gpr.key']
        }
   }
   ```
4. Add CustomWidgets as Dependency
   ```
   implementation 'de.domjos:customwidgets:2.1'
   ```

## Copyright

Copyright (C) 2017-2020  Dominic Joas

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 3
of the License, or (at your option) any later version.