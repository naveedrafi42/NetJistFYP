function httpGet(theUrl)
{
    var xmlHttp = null;

    xmlHttp = new XMLHttpRequest();
    xmlHttp.open( "GET", theUrl, false );
    xmlHttp.send( null );
	

    document.write (xmlHttp.responseText);
}
/*(function(){
	var app = angular.module('netjist',[]);
	
	app.controller('NewsfeedController',function(){
		this.topics = retrievedTopics;
	});
	
	app.controller('CollapseDemoCtrl', function ($scope) {
		  $scope.isCollapsed = false;
	});
	
	app.controller('ButtonsCtrl', function ($scope) {
		$scope.checkModel = {
				topicIsSelected : false
		};
	});
	
	
	var retrievedTopics = [
	    {
			title : 'BBC',
			links : [ {l:'link1'}, 
			          {l:'link2'},
			          ],
		},
		{
			title : 'Microsoft',
			links : [{l:'link3'}, 
			         {l:'link4'},
			         ],
		},
		{
			title : 'Bill Gates',
			links : [{l:'link3'},
			         {l:'link4'}, 
			         {l:'link5'},
			],
		},
		{
			title : 'Yemen',
			links : [{l:'link5'}, 
			         {l:'link8'},
			],
		},
		{
			title : 'KitKat',
			links : [{l:'link6'},
			         {l:'link7'},
			],
		},
	];
	
})();*/
