<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<c:url var="resources" value="/resources"/>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<script type="text/javascript" src="${ resources }/jquery-3.2.1.min.js"></script>
	<script type="text/javascript" src="${ resources }/ckeditor/ckeditor.js"></script>
	<script type="text/javascript" src="${ resources }/ckeditor/adapters/jquery.js"></script>
	<script type="text/javascript" src="${ resources }/jquery-ui.min.js"></script>
	<script type="text/javascript" src="${ resources }/jquery.tag-editor.min.js"></script>
	<script type="text/javascript" src="${ resources }/jquery.caret.min.js"></script>
	
	<link rel="stylesheet" href="${ resources }/jquery-ui.min.css">
	<link rel="stylesheet" href="${ resources }/jquery.tag-editor.css">
	<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
	<title>Edit</title>
</head>
<body>

	<div class="w3-container">
		<h1>Web-Application "spring-jpa-rest"</h1>
		<div id="warning" class="w3-panel w3-yellow w3-display-container">
			<p id="msg"></p>
		</div>
		<div id="recipe">
			<form action="javascript:save();" class="w3-container" id="recipeForm">
				<input type="hidden" id="uuid" name="uuid">
				<div class="w3-panel">
					<input class="w3-input" type="text" min="5" max="255" required id="title" name="title">
					<label class="w3-text-gray">Title</label>
				</div>
				<div class="w3-panel">
					<textarea name="preamble" id="preamble"></textarea>
					<label class="w3-text-gray">Preamble</label>
				</div>
				<div class="w3-panel">
					<label class="w3-text-gray">Zutaten für</label>
					<input class="w3-input" type="text" min="1" max="10" required id="noOfPerson" name="noOfPerson">
					<label class="w3-text-gray">Personen</label>
				</div>
				<div class="w3-panel">
					<ul class="w3-ul w3-light-gray" id="ingredients">
						<li><h3>Zutaten</h3></li>
					</ul>
				</div>
				<div class="w3-panel">
					<button class="w3-button w3-black" onclick="addIngredient()">add</button>
				</div>
				<div class="w3-panel">
					<textarea name="preparation" id="preparation"></textarea>
					<label class="w3-text-gray">Preparation</label>
				</div>
				<div class="w3-panel">
					<textarea name="tags" id="tags"></textarea>
					<label class="w3-text-gray">Tags</label>
				</div>
				<div class="w3-panel">
					<div class="w3-row-padding">
						<input class="w3-radio" type="radio" name="rating" value="1" checked><label>1 Stern</label>
						<input class="w3-radio" type="radio" name="rating" value="2" checked><label>2 Sterne</label>
						<input class="w3-radio" type="radio" name="rating" value="3" checked><label>3 Sterne</label>
						<input class="w3-radio" type="radio" name="rating" value="4" checked><label>4 Sterne</label>
						<input class="w3-radio" type="radio" name="rating" value="5" checked><label>5 Sterne</label>
					</div>
					<p class="w3-text-gray">Rating</p>
				</div>
				<div class="w3-panel"><button class="w3-button w3-red" type="submit">Speichern</button><span id="msg">&nbsp;</span></div>
			</form> 
		</div>
	</div>
	
	<script>
		function getRecipe( url ) {
			$.getJSON( url )
				.done( function( recipe ) { 
					buildRecipe( recipe );
					$.each(recipe.links, function( idx, link ) {
						if (link.rel == "ingredients") {
							getIngredients( link.href );	
						}
					});
				})
				.fail( function( xhr, status, error ) {
	  				var err = status + ", " + error;
	 				console.log( "Request Failed: " + err );
 				})
		}
		
		function getIngredients( url ) {
			$.getJSON( url )
				.done( function( ingredients ) {
					$.each( ingredients, function( idx, ingredient ) {
						buildIngredient( ingredient );	
		        	});
				})
				.fail( function( xhr, status, error ) {
   				    var err = status + ", " + error;
  					console.log( "Request Failed: " + err );
  				})
		}
		
		function buildIngredient( ingredient ) {

			li = $( "<li class='ingredient'>");
			li.attr({
				"id": ingredient.resourceId
			});
			li.appendTo( $("#ingredients" ));
			
			rowDiv = $("<div class='w3-row-padding'>");
			rowDiv.appendTo( li );
			
			/* ---------- Quantity */
			colQuantity = $("<div class='w3-col s3'>");
			colQuantity.appendTo( rowDiv );
			inputQuantity = $("<input class='w3-input' type='text' min='1' max='255' name='portion'>");
			if ( ingredient.portion ) {
				inputQuantity.val( ingredient.portion );
			}
			inputQuantity.appendTo( colQuantity );
			labelQuantity = $("<label class='w3-text-gray'>");
			labelQuantity.text( "Quantity" );
			labelQuantity.appendTo( colQuantity );
			
			/* ---------- Description */
			colDescription = $("<div class='w3-col s4'>");
			colDescription.appendTo( rowDiv );
			inputDescription = $("<input class='w3-input' type='text' min='1' max='255' required name='description'>");
			if ( ingredient.description ) {
				inputDescription.val( ingredient.description );
			}
			inputDescription.appendTo( colDescription );
			labelDescription = $("<label class='w3-text-gray'>");
			labelDescription.text( "Description" );
			labelDescription.appendTo( colDescription );
			
			/* ---------- Annotation */
			colAnnotation = $("<div class='w3-col s4'>");
			colAnnotation.appendTo( rowDiv );
			inputAnnotation = $("<input class='w3-input' type='text' min='1' max='255' name='comment'>");
			if ( ingredient.comment ) {
				inputAnnotation.val( ingredient.comment );
			}
			inputAnnotation.appendTo( colAnnotation );
			labelAnnotation = $("<label class='w3-text-gray'>");
			labelAnnotation.text( "Annotation" );
			labelAnnotation.appendTo( colAnnotation );
			
			/* ---------- Remove-Button */
			colRemove = $("<div class='w3-col s1'>");
			colRemove.appendTo( rowDiv );
			spanRemove = $("<span class='w3-button w3-white w3-xlarge w3-right  w3-light-gray'>&times;</span>")
			spanRemove.appendTo( colRemove );
			spanRemove.click( function() {
				if (ingredient.resourceId) {
					removeIngredient( ingredient.resourceId );
				} else {
					$( this ).closest( $( ".ingredient" ) ).fadeOut( "fast", function() {
						$( this ).closest( $( ".ingredient" ) ).remove();
					});
				}	
			})
		}
		
		function buildRecipe( recipe ) {
			$( "#recipe #uuid").val( recipe.uuid );
			$( "#recipe #title" ).val( recipe.title );
			$( "#recipe #preamble" ).val( recipe.preamble );
			$( "#recipe #noOfPerson" ).val( recipe.noOfPerson );
			$( "#recipe #preparation" ).val( recipe.preparation );
			$.each( recipe.tags, function( idx, tag ) {
				$( "#tags" ).tagEditor( "addTag", tag, false );
        	});
        	$( "input[name='rating'][value='" + recipe.rating + "']" ).prop( "checked", true );
        	console.log( "rating :" + recipe.rating );
		}
		
		function getRequestParams( k ){
			var p = {};
			location.search.replace( /[?&]+([^=&]+)=([^&]*)/gi, function( s,k,v )  { p[k] = v } )
			return k ? p[k] : p;
		}
		
		function removeIngredient( resourceId ) {
			console.log( "remove ResourceId: " + resourceId );
			$( ".ingredient#" + resourceId ).fadeOut( "fast", function() {
				$( ".ingredient" ).each( function( index ){
					console.log( index + ": " + $( this ).attr( "id" ) + " | Hidden: " + $( this ).is(":hidden") );
				});
			});
		}
	
		function addIngredient() {
			ingredient = {};
			buildIngredient( ingredient )
		}
		
		function save() {
			updateRecipe().then( function() {
				alert("Rezept gespeichert.");
			})
		}
		
		function updateRecipe() {
			var deferred = $.Deferred();
			$recipe = {};
			$recipe.uuid = $( "input[name='uuid']" ).val();
			$recipe.title = $( "input[name='title']" ).val();
			$recipe.preamble = CKEDITOR.instances['preamble'].getData();
			$recipe.noOfPerson = $( "input[name='noOfPerson']" ).val();
			$recipe.preparation = CKEDITOR.instances['preparation'].getData();
			$recipe.rating = $( "input[name='rating']:checked" ).val();
			$tags = $( "textarea[name='tags']" ).val();
			$recipe.tags = $tags.split(',');
			
			$json = JSON.stringify($recipe);
			console.log( "Recipe : " + $json );
			
			$.ajax({
				    url: "recipes/" + $recipe.uuid,
				    method: "PUT",
				    contentType: "application/json; charset=UTF-8",
				    data: $json
			})
			.fail( function( xhr, status, error ) {
   				    var err = status + ", " + error;
  					console.log( "Request Failed: " + err );
  			})
  			.then( function() {
  				updateIngredients( $recipe.uuid ).then( deferred.resolve );
  			})
			
			return deferred.promise();
		}
		
		function updateIngredients( recipeId ) {
			var deferred = $.Deferred();
			$( ".ingredient" ).each( function( index, li ) {
				$( "body" ).queue( function() {
					$ingredient = {};
					$ingredient.resourceId = $( li ).attr( "id" );
					$ingredient.portion = $( li ).find( "input[name='portion']" ).val();
					if ( $ingredient.portion == undefined ) {
						$ingredient.portion = null;
					}
					$ingredient.description = $( li ).find( "input[name='description']" ).val();
					$ingredient.comment = $( li ).find( "input[name='comment']" ).val();
					if ( $ingredient.comment == undefined ) {
						$ingredient.comment = null;
					}
				
					updateIngredient( $( li ).is(":hidden"), $ingredient, recipeId );
				})
			});
			$( "body" ).promise().done( deferred.resolve );
			return deferred.promise();
		}
		
		function updateIngredient( isHidden, ingredient, recipeId ) {
			$url = "recipes/" + recipeId + "/ingredients/";
			
			if ( isHidden ) {
				console.log( "Ingredient for delete : " + JSON.stringify( $ingredient ) );
				$.ajax({
					type: "DELETE",
					url: $url + $ingredient.resourceId,
					success: function() { $( "body" ).dequeue(); }
				});
			} else {
				if ( $ingredient.resourceId == undefined ) {
					console.log( "new Ingredient : " + JSON.stringify( $ingredient ) );
					$.ajax({
						type: "POST",
						url: $url,
						data: JSON.stringify( $ingredient ),
						contentType: "application/json; charset=UTF-8",
						success: function() { $( "body" ).dequeue(); }
					});
				} else {
					console.log( "exists Ingredient : " + JSON.stringify( $ingredient ) );
					$.ajax({
						type: "PUT",
						url: $url + $ingredient.resourceId,
						data: JSON.stringify( $ingredient ),
						contentType: "application/json; charset=UTF-8",
						success: function() { $( "body" ).dequeue(); }
					});
				}
			}
		}
		
		function init() {
			var deferred = $.Deferred();
			$.getJSON( "recipes/tags" )
				.done( function( tags ) { 
					$allTags = tags;
					$( "#tags" ).tagEditor( { 
						autocomplete: {
				        	source: $allTags,
				        	delay: 0,
			        		position: { collision: "flip" }
				    	},
				    	forceLowercase: true
				    } );
				})
				.fail( function( xhr, status, error ) {
	  				var err = status + ", " + error;
	 				console.log( "Request Failed: " + err );
	 				deferred.resolve;
 				})
 				.then( deferred.resolve );
 			return deferred.promise();
		}
		
		$( function() {
			$( "#msg" ).hide();
			CKEDITOR.replace( "preamble", {
			    language: "de",
			    contentsCss: "resources/ckEditorContents.css"
			});
			CKEDITOR.replace( "preparation", {
			    language: "de",
			    contentsCss: "resources/ckEditorContents.css"
			});
			init().then( function() {
				$id = getRequestParams( "id" );
				console.log( "id: " + $id );
				$url = "recipes/" + $id;
				getRecipe( $url );
			});
		});
		$( document ).ajaxError( function( event, request, settings ) {
			$( "#msg" ).show();
  			$( "#msg" ).text( "Error requesting page " + settings.url  );
		});
	</script>
</body>
</html>