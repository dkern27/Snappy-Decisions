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
		 The user can type the name of a product they wish to see into the text field. This will add the product to a list of 
		 various products, each with a button next to it. Clicking the button will take the user to an amazon page with that 
		 product as a search term.

		Originally this was going to use an Amazon API to get information about the product and then display a list of products 
		matching the search term so the user can compare them, but the API that we wanted to use is not available anymore so 
		this is a fix until a different API can be found and implemented.
		
		Currently Product information doesn't save when you go back to the menu

Known Bugs
	None

Areas we would like feedback on:
	Layout advice for random decision. We think space below buttons looks odd, add a rectangle outline for where decision output is?
	
	Layout advice for food decision. Would a small map view at the bottom be helpful? Please try to break the filters!

	We are thinking of getting rid of the main menu and using a hamburger naivgation drawer instead.
