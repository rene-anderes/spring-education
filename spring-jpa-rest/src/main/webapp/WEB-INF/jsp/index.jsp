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
				<p id="choice">Wähle ein Rezept aus der Liste aus ...</p>
				<div id="recipe">
				    <h1 id="title"></h1>
				    <p id="preamble"></p>
				    <h3>Zutaten für <span id="noofperson"></span> Personen</h3>
				    <p id="ingredients"></p>
				    <h3>Zubereitung</h3>
				    <p id="preparation"></p>
				    <p>Rating <span class="w3-badge" id="rating"></span></p>
				    <p class="w3-tiny">
				    	Erfasst: <span id="adding"></span><br>
				    	Aktualisiert: <span id="update"></span><br>
				    </p>
				    <p id="tags" class="w3-tiny">Stichworte:&nbsp;</p>
			    </div>
			</div>
		</div>
	</div>
	<script>
		var cookbook = {
			
			init: function() {
				$("#recipe").hide();
				$("#choice").show();
			},
			
			getRecipes: function() {
				$.getJSON("recipes")
					.done( function(json) { cookbook.handleRecipesList( json.content ) })
					.fail( function( xhr, status, error ) {
    				    var err = status + ", " + error;
   						console.log( "Request Failed: " + err );
	  				})
			},
			
			handleRecipesList: function( collection ) {
				$.each($(collection), function( idx, recipe ) {
					var url = "";
					$.each(recipe.links, function( idx, link ) {
						if (link.rel == "self") {
							url = link.href;	
						}
					});
					a = $( "<a>" ).attr( "href", "#" );
					a.text( recipe.title );
					a.click( function() {
						$("#recipe").hide();
						cookbook.getRecipe( url );
					});
					li = $( "<li>" ).append( a );
					li.appendTo( "#list" );
				});
			}, 
			
			getRecipe: function( url ) {
				$.getJSON( url )
					.done( function( recipe ) { 
						cookbook.buildRecipe( recipe );
						$.each(recipe.links, function( idx, link ) {
							if (link.rel == "ingredients") {
								cookbook.getIngredients( link.href );	
							}
						});
					})
					.fail( function( xhr, status, error ) {
    				    var err = status + ", " + error;
   						console.log( "Request Failed: " + err );
	  				})
			
			},
			
			getIngredients: function( url ) {
				$.getJSON( url )
					.done( function( ingredients ){
						cookbook.buildIngredients( ingredients );
						$("#choice").hide();
						$("#recipe").fadeIn();
					})
					.fail( function( xhr, status, error ) {
    				    var err = status + ", " + error;
   						console.log( "Request Failed: " + err );
	  				})
			},
			
			buildRecipe: function( recipe ) {
				$("#recipe #title").html( recipe.title );
				$("#recipe #preamble").html( recipe.preamble );
				$("#recipe #noofperson").text( recipe.noOfPerson );
				$("#recipe #preparation").html( recipe.preparation );
				$("#recipe #rating").html( recipe.rating );
				$("#recipe #adding").text( cookbook.formatDate(recipe.addingDateTime) );
				$("#recipe #update").text( cookbook.formatDate(recipe.lastUpdateTime) );
				$("#tags span").remove();
				$.each( recipe.tags, function(idx, tag) {
            		$("#tags").append("<span class='w3-tag'>" + tag + "</span>&nbsp;");
        		});
				
			},
			
			formatDate: function( number ) {
				$myDate = new Date(number);
        		return $myDate.toLocaleString();
        	},
			
			buildIngredients: function( ingredients ) {
				ingredients.sort(function(a, b){
					var a1= a.description, b1 = b.description;
					if( a1== b1 ) return 0;
					return a1 > b1 ? 1: -1;
				});
				$("#ingredients table").remove();
				ingredientsTable = $("<table>").attr( {
        			"class" : "w3-table w3-bordered",
        	        "style" : "width: 50%;"
        		} );
				$("#ingredients").append( ingredientsTable )
				$.each( ingredients, function( idx, ingredient ) {
					var tr = $( "<tr>" );
        			var tdPortion = $( "<td>" ).attr( {
        				"style" : "white-space:nowrap;"
        			} );
        			if ( ingredient.portion ) {
        				tdPortion.text( ingredient.portion );
        			}
        			var tdDescr = $( "<td>" );
        			if ( ingredient.comment ) {
        				tdDescr.html( ingredient.description + " " + ingredient.comment );
        			} else {
        				tdDescr.html( ingredient.description );
        			}
        			var tdSpace = $( "<td>" ).html( "&nbsp;" );
        			tr.append( tdPortion );
        			tr.append( tdSpace );
        			tr.append( tdDescr );
        			$("#ingredients table").append( tr );
		        });
			}
		};
	
	$(function() {
		cookbook.init();
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