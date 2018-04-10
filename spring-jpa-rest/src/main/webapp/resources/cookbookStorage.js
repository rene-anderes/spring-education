
var cookbook = ( function() {
	
	var $rootUrl = "/spring-jpa-rest"
	var $recipesUrl = $rootUrl + "/recipes";
	
	var getRecipesUrl = function( pageNo, pageSize ) {
		return $recipesUrl + "?sort=title&page=" + pageNo + "&size=" + pageSize;
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
		getRecipesUrl: getRecipesUrl
	}
})();