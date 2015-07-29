# Kitchen Sink

This is the complementary Android App for the CMP-464/788 Android course.

This course was taken @CUNY Lehman College Spring 15

kitchensink (as the name implies) is a giant mish-mosh of things we learned during the course

From the "hamburger" nav:

Kitchen Sink: just some background on the class

Hello World: self explanatory

lab 0: our first real project.  We wanted to retrieve data from /r/androiddev and display the items on the screen.

main ideas we wanted to learn with this were:
1. using an adapter
2. handling a .json file
3. using intents to go fetch data from the reddit api
4. onClickListener()
5. AsyncTask and why we need it vs using multiple threads
6. the purpose of onPostExecute() when using AsyncTask & doInBackGround

lab 1: We wanted to get our gps location (if the device is capable), or be able to manually input the latitude/longitude so we could see the location via google maps.  lessons for this lab:

1. using a built in android package (locationManager)
2. using services to run in the background (for this app, the gps)
3. using a broadcast receiver to "catch" the results from the gps and udate the map image

lab 2: the goal was to get some exposure to images and manipulating them (making them move).  the idea was to create a fan that we could use on the hot NYC subway platforms (don't laugh).  here we would need:

1. custom view
2. bitmap factory & paint
3. onDraw() and how it is constantly "drawing"
 
lab 3 was supposed to be working with SQLite and lab 4 was supposed to be final project of our choosing.  Unfortunately, we were running out of time in the semester, so we were allowed to combine the two.  you can find my final project in the TaskHelper repo --> https://github.com/Weffy/TaskHelper

