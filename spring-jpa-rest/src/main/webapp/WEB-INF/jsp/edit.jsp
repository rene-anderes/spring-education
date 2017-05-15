<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<script type="text/javascript" src="resources/jquery-3.2.1.min.js"></script>
	<script type="text/javascript" src="resources/ckeditor/ckeditor.js"></script>
	<script type="text/javascript" src="resources/ckeditor/adapters/jquery.js"></script>
	<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
	<title>Edit</title>
</head>
<body>

	<div class="w3-container">
		<h1>Web-Application "spring-jpa-rest"</h1>
		<div id="recipe">
			<form class="w3-container">
				<div class="w3-panel">
					<input class="w3-input" type="text" id="title" name="title">
					<label class="w3-text-gray">Title</label>
				</div>
				<div class="w3-panel">
					<textarea name="preamble" id="preamble"></textarea>
					<label class="w3-text-gray">Preamble</label>
				</div>
				<div class="w3-panel">
					<label class="w3-text-gray">Zutaten für</label>
					<input class="w3-input" type="text" id="noOfPerson" name="noOfPerson">
					<label class="w3-text-gray">Personen</label>
				</div>
				<div class="w3-panel">
					<h3>Zutaten</h3>
				</div>
				
				<div class="w3-panel">
					<ul class="w3-ul" style="width:75%">
						<li id="ingredient">
							<div class="w3-row">
								<div class="w3-col s3">
									<input class="w3-input" type="text" id="quantity" name="quantity">
									<label class="w3-text-gray">Quantity</label>
								</div>
								<div class="w3-col s4">
									<input class="w3-input" type="text" id="description" name="description">
									<label class="w3-text-gray">Description</label>
								</div>
								<div class="w3-col s4">
									<input class="w3-input" type="text" id="annotation" name="annotation">
									<label class="w3-text-gray">Annotation</label>
								</div>
								<div class="w3-col s1">
									<span onclick="removeIngredient(this)" class="w3-button w3-white w3-xlarge w3-right">&times;</span>
								</div>
							</div> 
						</li>
					</ul>
				</div>
				<div class="w3-panel">
					<textarea name="preparation" id="preparation"></textarea>
					<label class="w3-text-gray">Preparation</label>
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
			</form> 
		</div>
	</div>
	
	<script>
		function handleRecipe( url ) {
			$.getJSON( url )
				.done( function( recipe ) { 
					buildRecipe( recipe );
					$.each(recipe.links, function( idx, link ) {
						if (link.rel == "ingredients") {
							//getIngredients( link.href );	
						}
					});
				})
				.fail( function( xhr, status, error ) {
	  				var err = status + ", " + error;
	 				console.log( "Request Failed: " + err );
 				})
		}
		
		function buildRecipe( recipe ) {
			$( "#recipe #title" ).val( recipe.title );
			$( "#recipe #preamble" ).val( recipe.preamble );
			$( "#recipe #noOfPerson" ).val( recipe.noOfPerson );
			$( "#recipe #preparation" ).val( recipe.preparation );
			console.log( "recipe: " + recipe.title );
		}
		
		function getRequestParams( k ){
			var p = {};
			location.search.replace( /[?&]+([^=&]+)=([^&]*)/gi, function( s,k,v )  { p[k] = v } )
			return k ? p[k] : p;
		}
		
		function removeIngredient( object ) {
			console.log("Action here .....");
		}
	
		$(function() {
			CKEDITOR.replace( "preamble", {
			    language: "de",
			    contentsCss: "resources/ckEditorContents.css"
			});
			CKEDITOR.replace( "preparation", {
			    language: "de",
			    contentsCss: "resources/ckEditorContents.css"
			});
			$id = getRequestParams( "id" );
			console.log( "id: " + $id );
			$url = "recipes/" + $id;
			handleRecipe( $url );
		});
	</script>
</body>
</html>