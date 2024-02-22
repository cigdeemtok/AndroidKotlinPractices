This is very elementary clone of social media app Instagram. This app is made to practice mostly Firebase.

I used Firebase Authentication for login/logout. Firestore and Storage are used as real-time database. Also for image uploading, I used Picasso. 

In this app, we simply have three screens and these are all activities. When you first open the app, we have a simple login screen. You can sign in if you have an account or sign up if you don't have one. When you sign in, app remembers you until you sign out thanks to Firebase. We have a feed screen to display all posts This screen also has a menu to add post and log out. Add post screen has an image view to select image from gallery and description for your posts. We must give gallery permission to select image for our posts. These images are uploaded to Storage and all post information like user name, post date, image url and description are stored in Firestore. We can see all posts ordered by date in real time on the feed screen. Also post item design made custom using Card View.

You can see images from app below: 

<p float="left">
  <img src="https://github.com/cigdeemtok/AndroidKotlinPractices/blob/main/images/InstaCloneOne.jpg" width="33%" />
  <img src="https://github.com/cigdeemtok/AndroidKotlinPractices/blob/main/images/InstaClonePermission.jpg" width="33%" />
  <img src="https://github.com/cigdeemtok/AndroidKotlinPractices/blob/main/images/InstaCloneThree.jpg" width="33%" />
  <img src="https://github.com/cigdeemtok/AndroidKotlinPractices/blob/main/images/InstaCloneTwo.jpg" width="33%" />
  <img src="https://github.com/cigdeemtok/AndroidKotlinPractices/blob/main/images/InstaCloneFour.jpg" width="33%" />
</p>

You can check out codes and comments in them to have better understanding about how it works.
