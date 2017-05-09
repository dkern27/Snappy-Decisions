# Snappy-Decisions
#
#                _.'^^'.    
#     _      _.-' ((@)) '.   ./\/\/\/\/\/\,.---.__
#  ..'o'...-'      ~~~    '~/\/\/\/\/\/\__.---.   `-._
# :                          /\/\/\/\,-'              `-.__   
# ^VvvvvvvvvvvVvVv                   |                     `-._
#   ;^^^^^^^^^^^`      /             `\        /               `-._
#    ```````````````'.`                `\     (                    `'-._
#             .-----'`   /\              `\    )--.______.______._______`/ 
#            (((------'``  `'--------'`(((----'

Usage Instructions

App does not have a landscape view, it is locked into portrait.
Database only implemented for random decision.
Data not saved when moving between decision activities.

	Random Decision: 
		Fully implemented except for removing saved decisions
		
	Food Decision:
		
		The distance slider limits the restaurant search to restaurants that are a certain linear distance away (can be a little more with roads).
		Filters apply type of food, or pricing.
		Click make decision to randomly choose a restaurant (Note that the business count is after yelp does some magic in narrowing all restaurants from 1000 to 50, and then more filtering is applied in the app)
		Click Map it to open up the location via maps
		Click Yelp to open up the restaurants yelp page
	
		The location for finding restaurants is currently hardcoded into Golden, so if using an emulator, change the emulator location to:
			Longitude: -105.223
			Latitude: 39.7555
		This is done by clicking the ... button and changing the values in the Location tab. Otherwise youll probably be in california looking at
		restaurants really far away. Using a physical device will be fine, just note that the distance slider is relative to golden, not the
		phone's current location for this build.
		
		Currently saving/loading filter options is not functional, and filters are lost when going back to the main menu.
			
	Product Decision:
		 
		 The user can add products to a list of products. Within the list, there is a button next to each product that leads to a website selection screen. At this screen, the user
		 can choose which website they want to saerch for the product in (one of Amazon, Newegg, and eBay). Clicking the buttons for each website will open that site in a WebView.
		 Within the WebView, the user can add more products to their list as they see them appear on the websites, allowing them to narrow down a search as they go.

		 At the main product list screen, the user can send the list to the Random Decision screen by saving it and then loading it in the Random Decision screen. This allows the user
		 to have the app choose a product for them if they still can't pick one after getting the available information for each of them.

Known Bugs
	None
