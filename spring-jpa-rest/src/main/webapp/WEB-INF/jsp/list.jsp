<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<script src="resources/jquery-3.2.1.min.js"></script>
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<title>Liste</title>
</head>
<body>
	<div class="w3-container">
		<h1>Web-Application "spring-jpa-rest"</h1>
		<div class="w3-row">
			<div class="w3-col s6">
				<p>Liste aller Rezepte <span id="loading"><i class="fa fa-refresh"></i></span></p>
				<ul class="w3-ul" style="width:75%" id="list"></ul>
			</div>
			<div class="w3-col s6">
				<p>Wählen Sie ein Rezept aus der Liste aus ...
			</div>
		</div>
	</div>
	<script>
		var cookbook = {
			
			getRecipes: function() {
				$.getJSON("recipes")
					.done( function(json) { cookbook.handleRecipesList( json.content ) })
					.fail( function( xhr, status, errorThrown ) {
    				    var err = textStatus + ", " + error;
   						console.log( "Request Failed: " + err );
	  				})
			},
			
			handleRecipesList: function( collection ) {
				var url = "";
				$.each($(collection), function( idx, recipe ) {
					$.each(recipe.links, function( idx, link ) {
						if (link.rel == "self") {
							url = link.href;	
						}
						console.log( "url: " + url  );
					});
					var a = $( "<a></a>" ).attr( "href", "#" );
					a.text( recipe.title );
					a.click(function() {
						cookbook.getRecipe( url );
					});
					var li = $( "<li></li>" ).append( a );
					li.appendTo( "#list" );
				});
			}, 
			
			getRecipe: function( url ) {
				$.getJSON( url )
					.done( function(json) { 
						console.log("action goes here!!") 
					})
					.fail( function( xhr, status, errorThrown ) {
    				    var err = textStatus + ", " + error;
   						console.log( "Request Failed: " + err );
	  				})
			
			}
		};
	
	$(function() {
		cookbook.getRecipes();
	});
	$(document).ajaxStart(function(){
    	$("#loading").show();
	}); 
	$(document).ajaxStop(function(){
    	$("#loading").hide();
	});	
	</script>
</body>
</html>