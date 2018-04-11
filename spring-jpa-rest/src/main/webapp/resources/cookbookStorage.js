
var cookbookAPI = ( function() {
	
	var $rootUrl = "/spring-jpa-rest"
	var $recipesUrl = $rootUrl + "/recipes";
	var $springInfoUrl = $rootUrl + "/tech/info/spring";
	var $databaseInfoUrl = $rootUrl + "/tech/info/database";
	
	var getRecipesRootUrl = function() {
		return $recipesUrl;
	}
	
	var getRecipesUrl = function( pageNo, pageSize ) {
		return $recipesUrl + "?sort=title&page=" + pageNo + "&size=" + pageSize;
	}
	
	var getRecipeUrl = function( recipeId ) {
		return $recipesUrl + "/" + recipeId;
	}
	
	var getSpringInfoUrl = function() {
		return $springInfoUrl;
	}
	
	var getDatabaseInfoUrl = function() {
		return $databaseInfoUrl
	}
	var getTagsUrl = function() {
		return $recipesUrl + "/tags";
	}
	
	var load = function( url ) {
		var deferred = $.Deferred();
		console.log( "load data from: " + url);
		$.getJSON( url )
			.done( function( ingredients ) {
				deferred.resolve( ingredients );
			})
			.fail(function(xhr, status, error) {
				var err = "Request Failed: " + status + ", " + xhr.status + ", " + error;
				console.log(err);
				deferred.reject( err );
			})
		return deferred.promise();
	};
	
	return {
		load: load,
		getRecipesUrl: getRecipesUrl,
		getRecipeUrl: getRecipeUrl,
		getSpringInfoUrl: getSpringInfoUrl,
		getDatabaseInfoUrl: getDatabaseInfoUrl,
		getTagsUrl: getTagsUrl,
		getRecipesRootUrl: getRecipesRootUrl
	}
})();