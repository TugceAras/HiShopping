# HiShopping
The HiShopping application includes the most important and up-to-date services for e-commerce applications. Today's e-commerce applications are examined and features with the most up-to-date technologies have been added to our reference application. As described in next chapters of document, HiShopping reference application includes independent use case scenarios which can be developed by current mobile technologies and services. Each use case targets specific point of e-commerce applications such as customer acquiring, customer attracting, customer retrieval, customer targeting and etc.
It designed with modern and common-wide mobile technologies to align with real e-commerce applications. All design supports and show how to use multiple mobile service providers in modern applications to decrease one source dependency. In addition, application is designed to guide both decision makers and software developers at the same time. Decision makers can see how attract their customers with small touches of existing application. On the other hand, software developers can refer to applications and architecture to understand how Huawei Mobile Services can be worked with other existing services in Harmony.
In this application, you can test special products with Huawei AR technology in the environment you want, also with Huawei ML Kit service, besides product advisor and comment translate works, Huawei Awareness service can monitor user behavior or location and suggest products to the user. The purpose of development of HiShopping Reference App is to demonstrate the possible usage scenarios of HMS kits and services in an e-commerce app.
Each use case described in detail in further chapters in sub topics; short description, advantages of use case, limitation and future plans about use case, used services and libraries, supported mobile services.

## Use Cases
In this section, you can find more detail about each use cases which developed in HiShopping reference application.

## 1. Intelligent Product Advisor with Awareness Service
### a. Short Description
HUAWEI Awareness Kit provides your app with the ability to obtain contextual information including users' current time, location, behavior, audio device status, ambient light, weather, and nearby beacons. In this app with the Awareness service, products are recommended according to the user's behavior and weather in the user's location. In this app makes all suggestions on the client side by using the Huawei Awareness service. While making these suggestions, it makes suggestions by evaluating the user's Behavior Status-Weather Status-Headset Status and Time Status data with a smart algorithm. It differs from the product suggestions that are used normally, and we determine which products they need by evaluating the current situation of the user and the actions he usually takes.
### b. Advantages
Awareness service enables the user to reach the products more effectively and easily. This app can gain insight into a user's current situation more efficiently, making it possible to deliver a smarter, more considerate user experience.
### c. Screenshots & Gifs Files

<img src="https://user-images.githubusercontent.com/26767657/112137816-cab9aa80-8be1-11eb-8f2a-3a74518379cd.gif" width=250px>
   
### d.	Limitations & Future Works

* 	Awareness Kit supports wearable Android devices, but HUAWEI HMS Core 4.0 is not deployed on devices other than mobile phones. Therefore, wearable devices are not supported currently.
*  	An app can register a maximum of 200 barriers on a mobile phone. Barriers listen to the events created in the application and trigger them in the desired condition.

### e.	Used Services & Libraries
  
 [Huawei Awareness Service](https://developer.huawei.com/consumer/en/hms/huawei-awarenesskit/)

 [Google Awareness Service](https://developers.google.com/awareness/overview)

### f.	Supported Service Providers

| HMS | GMS |
| ------ | ------|
| YES | YES |



## 2.	Easy Register & Login with Most Used Providers
### a. Short Description
Most apps need to identify and authenticate users to provide better user experience. Building such a system from scratch is a difficult process. Easy sign in/out and register use case shows us to do these operations quickly by using Huawei and Google Auth Services. Users can sign in with their Huawei ID, Google and Facebook Account also they can quickly register with their e-mail address.
### b. Advantages
This use case allows users to sign into applications with their desired accounts without filling long forms. On the other hand, developers can focus their applications instead of making extra effort to handle sign in process, user authentication and securely store user information.
### c. Screenshots & Gifs Files

<table>
  <tbody>
    <tr>
      <td><img src="https://user-images.githubusercontent.com/26767657/112140237-d2c71980-8be4-11eb-96b9-9c9e088ee099.gif" width=250px></td>
      <td><img src="https://user-images.githubusercontent.com/26767657/112140249-d490dd00-8be4-11eb-8408-67a29ba4e16d.gif" width=300px></td>
    </tr>
  </tbody>
 </table>
   
### d.	Limitations & Future Works

* Huawei id can be used only with devices include HMS. Google Account can be used only with devices include GMS.

### e.	Used Services & Libraries
  
 [Huawei Account Kit](https://developer.huawei.com/consumer/en/hms/huawei-accountkit/)

 [Huawei Auth Service](https://developer.huawei.com/consumer/en/agconnect/auth-service/)
 
 [Google Sign in](https://developers.google.com/identity/sign-in/android/start-integrating)
 
 [Firebase Auth Service](https://firebase.google.com/docs/auth)
 
 [Facebook Login](https://developers.facebook.com/docs/facebook-login/android)

### f.	Supported Service Providers

| HMS | GMS |
| ------ | ------|
| YES | YES |



## 3. Increase Customer Experience with AR technology
### a. Short Description
The Face View feature provided by the Scene Kit gives your application the ability to position 3D objects on the user's face. In this way, your application will have the capabilities of users to test sunglasses. In addition, with the AR View feature, your application will have AR capability. With this feature, for example, the user can place a decorative product to be purchased anywhere in his home or office thanks to AR. In addition, thanks to the Scene View feature, your application will have the ability to visualize objects 360 degrees. Thanks to this feature, the user can view an object 360 degrees.
### b. Advantages
Scene Kit features make it easier for the user to visualize the products they will buy. In this way, the app enables the user to be able to test the product that will be purchased and provides a more satisfied shopping experience. In addition, the Scene Kit enables features such as AR, which are normally very complicated to implement into the application, much more easily. In other words, Scene Kit brings 360 degree product viewing, 3D product viewing and augmented reality features to your application much more effective and easy way.
### c. Screenshots & Gifs Files

<table>
  <tbody>
    <tr>
      <td><img src="https://user-images.githubusercontent.com/26767657/112142562-cc866c80-8be7-11eb-94b5-a4ac8b17f620.png" width=250px></td>
      <td><img src="https://user-images.githubusercontent.com/26767657/112142568-cdb79980-8be7-11eb-8916-1ff99f2448f5.png" width=250px></td>
      <td><img src="https://user-images.githubusercontent.com/26767657/112142569-cee8c680-8be7-11eb-977d-cac341711089.png" width=250px></td>
      <td><img src="https://user-images.githubusercontent.com/26767657/112142575-cf815d00-8be7-11eb-9103-ebf73c3fa235.png" width=250px></td>
    </tr>
  </tbody>
 </table>
   
### d.	Limitations & Future Works

* Scene Kit’s AR View feature supports only Huawei devices with EMUI version 9.1 or later.
* Face View feature supports only Huawei devices with EMUI version 10.0 or later. 
* The Scene View feature supports Huawei devices with EMUI version 3.0 or later.
* To render with AR View and Face View features, you need to use 3D models with the “.glb” format
* In the Scene View feature, you need to use “.dds” format for skybox materials.
* We planned to add the ability to try clothes in AR with AR Engine in the future version.

### Note: Please refer to [link](https://developer.huawei.com/consumer/en/doc/development/HMSCore-Guides-V5/features-0000001060501339-V5) for more information about AR Engine supported devices.


### e.	Used Services & Libraries
  
 [Huawei Scene Kit](https://developer.huawei.com/consumer/en/hms/huawei-scenekit/)


### f.	Supported Service Providers

| HMS | GMS |
| ------ | ------|
| YES | NO |




## 4. Find out Easily the Stores Where the Product is Available
### a. Short Description
By seeing your location information on the map of which store the product is located, you can instantly see the stores containing the product near you as a marker. Because it is a reference application, store locations are mock locations.
### b. Advantages
On the product detail page, the show icon opens a page. You can see your location as well as the stores that sell this product. If you click a location in the list or the navigation button, you will be redirected to the device's default navigation app to see navigation in the selected store (Huawei devices will be redirected to Petal Maps). Instead of visiting stores where the product is not available for the product that the user wants, he can go to the store where the product is located, thus saving time.
### c. Screenshots & Gifs Files

<img src="https://user-images.githubusercontent.com/26767657/112143627-35baaf80-8be9-11eb-9405-8ca7b11cb03e.gif" width=250px>
   
### d.	Limitations & Future Works

* It can run on devices that have Android version 5.0 or above. In order to see your location your GPS service must be enabled and location permission must be given for Android version 5.0 or above devices.


### e.	Used Services & Libraries
  
 [Huawei Location Kit](https://developer.huawei.com/consumer/en/hms/huawei-locationkit/)
 
 [Huawei Map Kit](https://developer.huawei.com/consumer/en/hms/huawei-MapKit/)
 
 [Google Location Service](https://developer.android.com/training/location)
 
 [Google Map Service](https://developers.google.com/maps/gmp-get-started)
 


### f.	Supported Service Providers

| HMS | GMS |
| ------ | ------|
| YES | YES |




## 5. Explore stores near me
### a. Short Description
The map of stores near you shows the user's location in the map application, and store markers are updated depending on the location. Store locations are mock locations for the reference application.
### b. Advantages
When you select “stores” on the profile page, the map page opens. You can see your location and the stores closest to you. If you click a location in the list or the navigation button, you are redirected to the device's default navigation app to see navigation in the selected store, and on Huawei devices, you are redirected to Petal Maps. Referral to physical stores is provided for products that are not in stock online.
### c. Screenshots & Gifs Files

<img src="https://user-images.githubusercontent.com/26767657/112143720-52ef7e00-8be9-11eb-8b0d-0cb885ce7c18.gif" width=250px>
   
### d.	Limitations & Future Works

* It can run on devices that have Android version 5.0 or above. In order to see your location your GPS service must be enabled and location permission must be given for Android version 5.0 or above devices.


### e.	Used Services & Libraries
  
 [Huawei Location Kit](https://developer.huawei.com/consumer/en/hms/huawei-locationkit/)
 
 [Huawei Map Kit](https://developer.huawei.com/consumer/en/hms/huawei-MapKit/)

 [Huawei Crash Kit](https://developer.huawei.com/consumer/en/doc/development/AppGallery-connect-Guides/agc-crash-introduction-0000001055732708)

 [Google Location Service](https://developer.android.com/training/location)
 
 [Google Map Service](https://developers.google.com/maps/gmp-get-started)

 [Firebase Crashlytics](https://firebase.google.com/docs/crashlytics/get-started?platform=android)



### f.	Supported Service Providers

| HMS | GMS |
| ------ | ------|
| YES | YES |




## 6. Identity Your Debit & Credit Cards in seconds
### a. Short Description
23% of users are lazy to enter their card information, and many users doubt the security of such services, but do not allow you to get a screenshot or screen record from within the user or application in any way. A use case used to quickly and accurately add users ' card information to the application.
### b. Advantages
The service can be accessed as follows: go to the profile page, click on my credit cards after entering the account, then click the Add Card button in the lower right corner and scan the card. Users lose time when entering card information, and in addition, they may enter card information incorrectly, which prevents the use case created to prevent this from leading to a loss of time. Your card information is added in as little as 2 seconds. In addition, the importance of security is quite strong, as we mentioned in short description.
### c. Screenshots & Gifs Files

<img src="https://user-images.githubusercontent.com/26767657/112147168-71577880-8bed-11eb-9123-b308a0171145.jpg" width=250px>


**Note: We are not allowed to take screenshots or videos when scanning on camera for security reasons. You can try it yourself.**
   
### d.	Limitations & Future Works

* It can run on devices that have Non-Huawei phones: Android version 4.4 or later, Huawei devices: EMUI 4.4 or later.  In order to see scan your credit card via camera must be enabled camera permission for Android version 5.0 or above devices..


### e.	Used Services & Libraries
  
 [Huawei Scan Kit](https://developer.huawei.com/consumer/en/hms/huawei-scankit/)
 

### f.	Supported Service Providers

| HMS | GMS |
| ------ | ------|
| YES | NO |



## 7. Enrich Product Details with Video Services
### a. Short Description
HUAWEI Video Kit provides video playback in this version, and will support video editing and video hosting in later versions, helping you quickly build desired video features to deliver a superb video experience to your app users. In this application, users can obtain clearer and more detailed visual information about the product in product details.
### b. Advantages
Video Kit provides users with a quality video surveillance service. Video Kit provides users with a quality video surveillance service. This application provides users to watch product videos in a very practical way with the video kit.
### c. Screenshots & Gifs Files

**Note: Case is not active for this version of app.**

   
### d.	Limitations & Future Works

* For now, these features only work on HMS devices. The same features will be used in GMS in the future. Exo player will be used on GMS devices.


### e.	Used Services & Libraries
  
 [Huawei Video Kit](https://developer.huawei.com/consumer/en/hms/huawei-videokit/)
 

### f.	Supported Service Providers

| HMS | GMS |
| ------ | ------|
| YES | NO |



## 8. Easy Address Management with Identity Service

### a. Short Description
Identity Kit provides unified address management services for users and allows your app to access users' addresses conveniently upon obtaining authorization from the users.

### b. Advantages
Users who have added their addresses to their Huawei account can quickly select their addresses from their Huawei account instead of adding them again for each application. This use case shows us how to use Huawei identity kit in android application easily.

### c. Screenshots & Gifs Files
<img src="https://user-images.githubusercontent.com/26767657/112143794-6f8bb600-8be9-11eb-84b0-adc8f265f340.gif" width=250px>

### d.	Limitations & Future Works
* Huawei Identity service only accessible in Chile, Chinese mainland, Colombia, Denmark, Egypt, France, Germany, India, Italy, Jamaica, Japan, Kenya, Malaysia, Mexico, Norway, Philippines, Poland, Russia, Saudi Arabia, Singapore, South Africa, Spain, Sweden, Thailand, Turkey, United Arab Emirates, United Kingdom.
* This use case only used with devices with HMS.

### e.	Used Services & Libraries
[Huawei Identity Kit](https://developer.huawei.com/consumer/en/hms/huawei-identitykit/)
 

### f.	Supported Service Providers
| HMS | GMS |
| ------ | ------|
| YES | NO |




## 9. Easy Address Management with Map Service

### a. Short Description
This use case allows users to select their address from map service. By using map services and android geocoder, the location selected by user is converted to the address and is added to the user’s addresses.

### b. Advantages
Users can add their address without filling out long forms.

### c. Screenshots & Gifs Files
<img src="https://user-images.githubusercontent.com/26767657/112143801-71557980-8be9-11eb-9238-5fdb4bfda031.gif" width=250px>

### d.	Limitations & Future Works
* Both GMS and HMS devices are supported.

### e.	Used Services & Libraries
 [Huawei Location Kit](https://developer.huawei.com/consumer/en/hms/huawei-locationkit/)
 
 [Huawei Map Kit](https://developer.huawei.com/consumer/en/hms/huawei-MapKit/)
 
 [Google Location Service](https://developer.android.com/training/location)
 
 [Google Map Service](https://developers.google.com/maps/gmp-get-started)
 
 [Android Geocoder](https://developer.android.com/reference/android/location/Geocoder)
 

### f.	Supported Service Providers
| HMS | GMS |
| ------ | ------|
| YES | YES |



## 10. Intelligent Product Search with Voice & QR Code

### a. Short Description
This service uses industry-leading deep learning technologies to achieve a recognition accuracy of over 95%. Scan Kit helping you quickly build barcode scanning functions into your apps. In this application, these two features provide the user to search for products with less effort. As few actions as possible from the user are expected to improve the user experience. The feature of searching with barcode and converting voice to text is important in this respect.

### b. Advantages
Machine Learning ASR and Scan Kit can be used in many different cases. We made it easier to search products in the e-commerce application and increased the user experience. According to research, users over a certain age want to use the voice-to-speech feature, especially nowadays, where features such as speech to text are becoming more common and developed. We know that the barcode search feature is very common today. That's why these features are actually very important in terms of user experience

### c. Screenshots & Gifs Files
<img src="https://user-images.githubusercontent.com/26767657/112143865-86320d00-8be9-11eb-913f-85bac55c054c.gif" width=250px>

### d.	Limitations & Future Works
* For now, these features only work on HMS devices. The same features will be used in GMS in the future.
* Automatic speech recognition (ASR) can recognize speech not longer than 60s and convert the input speech into text in real time. Currently, Mandarin Chinese (including Chinese-English bilingual speech), English, French, German, Spanish, and Italian can be recognized.
* Scan Kit scans and parses all major 1D and 2D barcodes and generates QR codes.
* By taking a picture of any product around us, there will be a feature to search for similar products.

### e.	Used Services & Libraries
 [Huawei Machine Learning Kit](https://developer.huawei.com/consumer/en/hms/huawei-mlkit/)
 
 [Huawei Scan Kit](https://developer.huawei.com/consumer/en/hms/huawei-scankit/)
 
### f.	Supported Service Providers
| HMS | GMS |
| ------ | ------|
| YES | NO |




## 11. View product comments in any language you want

### a. Short Description
The real-time translation service can translate text from the source language into the target language through the server on the cloud. In this application, we used the real-time translation feature to translate users' product comments into the language of their choice. Whatever language the comment is written in, that language is analyzed. Afterwards, the translation process is done. It can translate different languages at the same time.

### b. Advantages
By translating the comments to their desired language in the application, we made the applications more global. No matter which country uses the e-commerce application, users can easily understand the comments.

### c. Screenshots & Gifs Files
<img src="https://user-images.githubusercontent.com/26767657/112143891-8c27ee00-8be9-11eb-830b-eae9bc3f1937.gif" width=250px>

### d.	Limitations & Future Works
* For now, these features only work on HMS devices. The same features will be used in GMS in the future.
* Currently, real-time translation supports 38 languages.


### e.	Used Services & Libraries
 [Huawei Machine Learning Kit](https://developer.huawei.com/consumer/en/hms/huawei-mlkit/)
 
 
### f.	Supported Service Providers
| HMS | GMS |
| ------ | ------|
| YES | NO |



## 12. Follow Your Order Instantly

### a. Short Description
It is important for the user to see in what state the orders You Give Online are in instantly. Because of this, it is a use case that we create for the user to instantly follow their order from the map application.

### b. Advantages
If the status of any of your products in the list is “move”, by clicking on it, the fake tracking location will be set, and over time, the fake location pointer set to your location will come to your location. In this way, it can instantly monitor the status of customer orders. Many ecommerce companies use this use case. It notifies the user with a warning that the order has reached the user, so the user is alerted.

### c. Screenshots & Gifs Files
<table>
  <tbody>
    <tr>
      <td><img src="https://user-images.githubusercontent.com/26767657/112160922-b08cc600-8bfb-11eb-8870-ce44523296ac.png" width=250px></td>
      <td><img src="https://user-images.githubusercontent.com/26767657/112160895-ab2f7b80-8bfb-11eb-8a5d-718c185baf02.png" width=250px></td>
      <td><img src="https://user-images.githubusercontent.com/26767657/112160915-af5b9900-8bfb-11eb-8596-a55661418596.png" width=250px></td>
    </tr>
  </tbody>
 </table>

### d.	Limitations & Future Works
* It can run on devices that have Android version 5.0 or above. In order to see your location your GPS service must be enabled and location permission must be given for Android version 5.0 or above devices.

### e.	Used Services & Libraries
 [Huawei Location Kit](https://developer.huawei.com/consumer/en/hms/huawei-locationkit/)
 
 [Huawei Map Kit](https://developer.huawei.com/consumer/en/hms/huawei-MapKit/)
 
 [Google Location Service](https://developer.android.com/training/location)
 
 [Google Map Service](https://developers.google.com/maps/gmp-get-started)
 
### f.	Supported Service Providers
| HMS | GMS |
| ------ | ------|
| YES | YES |

## 13. Make your application more secure.

### a. Short Description
With the Safety Detect feature, you can make your application more secure. You can quickly and easily check whether users are fake users and whether the device is safe.

### b. Advantages
You can quickly and easily check whether users are fake users and whether the device is safe. You can make your application more secure in both platform.

### c. Screenshots & Gifs Files
<table>
  <tbody>
    <tr>
      <td><img src="https://user-images.githubusercontent.com/26767657/114834449-3a821600-9dd9-11eb-9a88-9bfd116222d4.gif" width=250px></td>
      <td><img src="https://user-images.githubusercontent.com/26767657/114822222-66e26600-9dca-11eb-9d3c-cbaaf863d821.gif" width=250px></td>
      <td><img src="https://user-images.githubusercontent.com/26767657/114822225-68ac2980-9dca-11eb-8574-a9d9df2e2759.gif" width=250px></td>
      <td><img src="https://user-images.githubusercontent.com/26767657/114822350-972a0480-9dca-11eb-8d63-d69dbe7be791.gif" width=250px></td>
    </tr>
  </tbody>
 </table>

### d.	Limitations & Future Works
* Safety Detect can be used to check app security only in EMUI 5.0 and later. If you use it in versions earlier than EMUI 5.0, an empty list (no malicious app on the device) will be returned.

### e.	Used Services & Libraries
 [Huawei Safety Detect](https://developer.huawei.com/consumer/en/hms/huawei-safetydetectkit/)

 [Google SafetyNet](https://developer.android.com/training/safetynet)

### f.	Supported Service Providers
| HMS | GMS |
| ------ | ------|
| YES | YES |



## Project Structure
HiShopping is designed with modern libraries and technologies. Main design pattern of HiShopping application is MVVM. It designed in modular structure which totally separate service layer, app layer and use cases from each other’s. Thanks to this modular structure you can build your own app layer by using use case modules or you can develop your use case by using service layer modules without care multiple services. You can add new service provider in each service layer module according to your needs. So everything is flexible and suitable for your projects also.

<img src="https://user-images.githubusercontent.com/26767657/112162672-512fb580-8bfd-11eb-8db3-4083b8887fc7.png">



**Hardware Requirements**
 - A computer that can run Android Studio.
 - A Huawei phone for debugging.
 - AR supported Huawei devices.
 
**Software Requirements**
   - Android SDK package
   - Android Studio 3.X-4.X
   - HMS Core 4.X or later


## Libraries
There are some 3rd party libraries which used in project. All these libraries are mostly used in modern android application development by thousands of applications.


### 1. Common Libraries

#### a. Navigation Component
Navigation refers to the interactions that allow users to navigate across, into, and back out from the different pieces of content within your app. Android Jetpack's Navigation component helps you implement navigation, from simple button clicks to more complex patterns, such as app bars and the navigation drawer.

#### b. Hilt
Hilt provides a standard way to incorporate Dagger dependency injection into an Android application.

#### c. Koin
A pragmatic lightweight dependency injection framework for Kotlin developers.

#### d. Coroutines
Coroutines are computer program components that generalize subroutines for non-preemptive multitasking, by allowing execution to be suspended and resumed.

#### e. Gson
Gson is a Java library that can be used to convert Java Objects into their JSON representation. It can also be used to convert a JSON string to an equivalent Java object.

#### f. Coil
An image loading library for Android backed by Kotlin Coroutines. 

#### g. QuickPermission Kotlin
The easiest way to handle Android Runtime Permissions in Kotlin

#### h. Material Components
Material Components are interactive building blocks for creating a user interface. Browse all components or select a specific platform.


### 2. App Module

#### a. Page Indicator View
PageIndicatorView is light library to indicate ViewPager's selected page with different animations and ability to customize it as you need.

#### b. Android Device Names
A Library for devices market names to check AR supported or not.

#### c.	Simple Animation Popup
Showing popup with an animation view easily. It is based on Android.

#### d.	HMS Crash Kit
Detect your crashes in your app with HMS Crash Kit.

### 3. Data Module

#### a. Moshi
Moshi is a modern JSON library for Android and Java. It makes it easy to parse JSON into Java objects

#### b. Hawk
Secure, simple key-value storage for Android. Used for local storage in the apps.


### 4. Credit Cart Module

### a. Credit Card View
CreditCardView is an Android library that allows developers to create the UI which replicates an actual Credit Card.


## Download
To download and install Hi Shopping application you can download;

[HiShopping APK](https://onebox.huawei.com/p/e8146f483d5ef2c04e596a2272f6cbad)

## Contributors
-   Muhammed Salih Karakasli
-	Oguz Ozsoy
-	Omer Akkus
-	Mahmut Yetisir
-	Alperen Babagil
-	Murat Cakir
-	Abdulkadir Tas
-	Mahmut Can Sevin
-   Murat Yuksektepe
-   Sabrina Cara
-   Berk Avcioglu
-   Mustafa Kemal Ozdemir



