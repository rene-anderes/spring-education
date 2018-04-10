<%@ page language="java" session="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>

<% 
java.util.jar.Manifest manifest = new java.util.jar.Manifest();
manifest.read(pageContext.getServletContext().getResourceAsStream("/META-INF/MANIFEST.MF"));
java.util.jar.Attributes attributes = manifest.getMainAttributes();
request.setAttribute("release", attributes.getValue("Implementation-Version"));
%>

<c:url var="resources" value="/resources"/>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
	<script type="text/javascript" src="${ resources }/jquery-3.3.1.min.js"></script>
	<script type="text/javascript" src="${ resources }/ckeditor/ckeditor.js"></script>
	<script type="text/javascript" src="${ resources }/ckeditor/adapters/jquery.js"></script>
	<script type="text/javascript" src="${ resources }/jquery-ui.min.js"></script>
	<script type="text/javascript" src="${ resources }/jquery.tag-editor.min.js"></script>
	<script type="text/javascript" src="${ resources }/jquery.caret.min.js"></script>
	<script type="text/javascript" src="${ resources }/cookbookStorage.js"></script>
	
	<link rel="stylesheet" href="${ resources }/jquery-ui.min.css">
	<link rel="stylesheet" href="${ resources }/jquery.tag-editor.css">
	<link rel="stylesheet" href="${ resources }/w3.css">
	<link rel="stylesheet" href="${ resources }/font-awesome-4.7.0/css/font-awesome.min.css">
	<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Proza+Libre">
	<style>
		html, body, h1, h2, h3, h4, h5, h6 {
		    font-family: "Proza Libre", Verdana, Helvetica, sans-serif;
		}
	</style>
	<title>Kochbuch chez René</title>
</head>
<body>
	<!-- Sidebar -->
	<div class="w3-sidebar w3-bar-block w3-card-2 w3-animate-left w3-light-gray" style="display:none;left:0;" id="mySidebar">
	  <button class="w3-bar-item w3-button w3-red" onclick="w3_close()">Close</button>
	  <button class="w3-button w3-bar-item" onclick="dialogLogin.show()">Login</button>
	  <button id="addRecipe" class="w3-button w3-bar-item" title="neues Rezept erfassen">Neues Rezept</button>
      <button id="backupDatabase" class="w3-button w3-bar-item" title="Backup Database">Backup Database</button>
      <button id="about" class="w3-button w3-bar-item" title="About">About</button>
	</div>
	<script>
		function w3_open() {
			$("#mySidebar").css("display", "block");
		}
		function w3_close() {
			$("#mySidebar").hide();
		}
	</script>
	
	<!-- Page Content -->
	<header class="w3-bar w3-teal w3-xxlarge">
	 	<button class="w3-bar-item w3-button w3-teal" onclick="w3_open()">&#9776;</button>
  		<span class="w3-bar-item">Kochbuch chez René</span>
	</header> 

	<div class="w3-row">
		<div class="w3-third w3-container">
			<h3>Liste aller Rezepte <span id="loading"><i class="fa fa-refresh"></i></span></h3>
			<div id="recipes">
				<ul class="w3-ul w3-hoverable w3-large" id="list"></ul>
				<div class="w3-bar w3-center">
					<a id="prevPage" class="w3-button w3-xxlarge" title="previous">&laquo;</a>
					<a id="nextPage" class="w3-button w3-xxlarge" title="next">&raquo;</a>
				</div>
			</div>
		</div>
		<div class="w3-twothird w3-container">
			<p id="choice">Wähle ein Rezept aus der Liste aus ...</p>
			
			<!-- ************************** einzelnes Rezept anzeigen ********************* -->
			<div id="recipe">
			    <h1 id="title"></h1>
			    <p id="preamble"></p>
			    <h3>Zutaten für <span id="noofperson"></span> Personen</h3>
			    <p id="ingredients"></p>
			    <h3>Zubereitung</h3>
			    <p id="preparation"></p>
			    <p>Rating <span class="w3-badge w3-teal" id="rating"></span></p>
			    <p class="w3-tiny">
			    	Erfasst: <span id="adding"></span><br>
			    	Aktualisiert: <span id="update"></span><br>
			    </p>
			    <p id="tags" class="w3-tiny">Stichworte:&nbsp;</p>
			    <p id="resourceId"></p>
			    <div id="editAndDelete">
				    <hr>
			    	<form>
					    <p>
				    		<button id="edit" class="w3-button w3-red" type="submit">Editieren</button>&nbsp;
				    		<button id="delete" class="w3-button w3-red">Löschen</button>
					    </p>
			    	</form>
		    	</div>
		    </div>
		    
		    <!-- ************************** Rezept Editieren ****************************** -->
		    <div id="recipeEdit">
		    	<div id="recipe-edit">
					<form class="w3-container" id="recipeForm">
						<input type="hidden" id="uuid" name="uuid" value="${ recipeId }">
						<input type="hidden" id="token" name="token" value="${ token }">
						<div class="w3-panel">
							<input class="w3-input" type="text" min="5" max="255" required id="title" name="title">
							<label class="w3-text-gray">Title</label>
						</div>
						<div class="w3-panel">
							<textarea name="preamble" id="editPreamble"></textarea>
							<label class="w3-text-gray">Preamble</label>
						</div>
						<div class="w3-panel">
							<label class="w3-text-gray">Zutaten für</label>
							<input class="w3-input" style="width:25%" type="text" min="1" max="10" required id="noOfPerson" name="noOfPerson">
							<label class="w3-text-gray">Personen</label>
						</div>
						<div class="w3-panel">
							<ul class="w3-ul w3-light-gray" id="ingredients">
								<li><h3>Zutaten</h3></li>
							</ul>
						</div>
						<div class="w3-panel">
							<button class="w3-button w3-circle w3-red" id="addIngredient" title="Zutat hinzufügen">+</button>
						</div>
						<div class="w3-panel">
							<textarea name="preparation" id="editPreparation"></textarea>
							<label class="w3-text-gray">Preparation</label>
						</div>
						<div class="w3-panel">
							<textarea name="tags" id="tags"></textarea>
							<label class="w3-text-gray">Tags</label>
						</div>
						<div class="w3-panel">
							<div class="w3-row-padding">
								<input class="w3-radio" type="radio" name="rating" value="1" checked><label><span class="w3-badge w3-teal">1</span></label>
								<input class="w3-radio" type="radio" name="rating" value="2" checked><label><span class="w3-badge w3-teal">2</span></label>
								<input class="w3-radio" type="radio" name="rating" value="3" checked><label><span class="w3-badge w3-teal">3</span></label>
								<input class="w3-radio" type="radio" name="rating" value="4" checked><label><span class="w3-badge w3-teal">4</span></label>
								<input class="w3-radio" type="radio" name="rating" value="5" checked><label><span class="w3-badge w3-teal">5</span></label>
							</div>
							<p class="w3-text-gray">Rating</p>
						</div>
						<hr>
						<div class="w3-panel w3-row-padding">
							<div class="w3-col s2"><button class="w3-button w3-red" type="submit" id="save">Speichern</button></div>
							<div class="w3-col s2"><span class="w3-button w3-green" id="status"></span></div>
							<div class="w3-col s8"></div>
						</div> 
					</form> 
				</div>
		    </div>
		    
		</div>
	</div>
	<p>&nbsp;</p>
	<footer class="w3-container w3-teal">
		<h5>Cookbook</h5>
		<p>by René Anderes</p>
	</footer> 	
	
	<div id="dialogDelete" class="w3-modal">
		<div class="w3-modal-content w3-card-4 w3-animate-zoom" style="max-width:400px">
			<header class="w3-container w3-blue">
				<span onclick="dialogDelete.cancel();" class="w3-button w3-blue w3-xlarge w3-display-topright" title="Close Dialog">&times;</span>
				<h2>Confirmation Required</h2>
			</header>
			<div class="w3-container">
				<p>Das Rezept wirklich löschen?</p>
			</div>
			<div class="w3-container w3-light-grey w3-padding">
				<div class="w3-bar w3-right-align">
					<button class="w3-button w3-red" onclick="dialogDelete.confirm();">Ja</button>
					<button class="w3-button w3-red" onclick="dialogDelete.cancel();">Nein</button>
				</div>
			</div>
		</div>
	</div>
	
	<div id="dialogMessage" class="w3-modal">
		<div class="w3-modal-content w3-card-4 w3-animate-zoom" style="max-width:400px">
			<header class="w3-container w3-blue">
				<span onclick="dialogMessage.cancel();" class="w3-button w3-blue w3-xlarge w3-display-topright" title="Close Dialog">&times;</span>
				<h2>Meldung</h2>
			</header>
			<div class="w3-container">
				<p id="messageText"></p>
			</div>
			<div class="w3-container w3-light-grey w3-padding">
				<div class="w3-bar w3-right-align">
					<button class="w3-button w3-red" onclick="dialogMessage.cancel();">OK</button>
				</div>
			</div>
		</div>
	</div>
	
	<div id="dialogLogin" class="w3-modal">
		<div class="w3-modal-content w3-card-4 w3-animate-zoom" style="max-width: 500px">

			<div class="w3-center">
				<br><span onclick="dialogLogin.cancel();" class="w3-button w3-xlarge w3-hover-red w3-display-topright" title="Close Dialog">&times;</span>
				<img src="${ resources }/images/img_avatar.png" alt="Avatar" style="width: 30%" class="w3-circle w3-margin-top">
			</div>

			<form class="w3-container" action="javascript:dialogLogin.confirm();">
				<div class="w3-section">
					<label><b>Username</b></label>
					<input class="w3-input w3-border w3-margin-bottom" type="text" placeholder="Enter Username" name="username" required>
					<label><b>Password</b></label>
					<input class="w3-input w3-border" type="password" placeholder="Enter Password" name="password" required>
					<button class="w3-button w3-block w3-red w3-section w3-padding" type="submit">Login</button>
				</div>
			</form>

			<div class="w3-container w3-border-top w3-padding-16 w3-light-grey">
				<button onclick="dialogLogin.cancel();" type="button" class="w3-button w3-red">Cancel</button>
				<p id="error"></p>
			</div>
		</div>
	</div>
	<script>
		var $rootUrl = "/spring-jpa-rest"
		var $recipesUrl = $rootUrl + "/recipes";
		var $tokenUrl = "/spring-security-jwt-server/users/token";

		var dialogDelete = {

			cancel : function() {
				$("#dialogDelete").hide();
			},

			show : function() {
				$("#dialogDelete").show();
			},

			confirm : function() {
				$("#dialogDelete").hide();
				cookbook.deleteRecipe()
					.fail( function( message ) {
						dialogMessage.show( message );
					})
					.then( function() {
						recipe.init();
						recipes.show();
					});
			}
		}

		var dialogMessage = {

			show : function(message) {
				$("#dialogMessage #messageText").html(message);
				$("#dialogMessage").show();
			},

			cancel : function() {
				$("#dialogMessage").hide();
			},
			
			confirm : function() {
				$("#dialogMessage").hide();
			}
		}

		var dialogLogin = {
		
			cancel : function() {
				$("#dialogLogin").hide();
			},

			show : function() {
				$("#dialogLogin").show();
			},
			
			confirm : function() {
				var $user = $( "#dialogLogin input[name='username']" ).val();
				var $password = $( "#dialogLogin input[name='password']" ).val();
				user.login( $user, $password )
					.done( function( token ) {
						console.log( "Login Token: " + token );
						$( "#dialogLogin #error" ).text("");
						$( "#dialogLogin" ).hide();
					})
					.fail( function( message ) {
						$( "#dialogLogin #error" ).text("Anmeldung nicht erfolgreich: " + message );
					})
			}
		}
		
		var user = (function() {
			
			var token;
			var changeCallback;
			
			var login = function( user, password ) {
				var deferred = $.Deferred();
				$.ajax({
					url : $tokenUrl,
					method : "POST",
					headers: { 'Authorization': "Basic " + window.btoa(user + ":" + password) }
				})
				.done( function( json ) {
					token = json.token;
					changeCallback( token );
					deferred.resolve( token );
				})
				.fail( function( xhr, status, error ) {
					var err = "Request Failed: " + status + ", " + xhr.status + ", " + error;
					console.log( err );
					token = null;
					changeCallback( token );
					deferred.reject( err );
				});
				return deferred.promise();
			};
			
			var getToken = function() {
				return token;
			};
			
			var change = function( callback ) {
				changeCallback = callback;				
			};
			
			return {
				getToken: getToken,
				login: login,
				change: change
			}
		})();
		
		var recipes = (function() {
			var url;
			var pageSize = 10;
			var pageNo = 0;
			var showRecipeCallback;
			
			var init = function( recipesUrl, recipeHandler ) {
				url = recipesUrl;
				showRecipeCallback = recipeHandler;
				$( "#recipes" ).hide();
				$( "#nextPage" ).on( "click", function( e ) {
					e.preventDefault();
					pageNo++;
					$( "#recipes" ).hide();
					show();
				});
				$( "#prevPage" ).on( "click", function( e ) {
					e.preventDefault();
					if (pageNo > 0) {
						pageNo--;
						$( "#recipes" ).hide();
						show();
					}
				});
			};
		
			var show = function() {
				var deferred = $.Deferred();
				var completeUrl = url + "?sort=title&page=" + pageNo + "&size=" + pageSize;
				$.getJSON( completeUrl )
					.fail( function(xhr, status, error) {
						var err = "Request Failed: " + status + ", " + xhr.status + ", " + error;
						console.log( err );
						deferred.reject( err );
					})
					.then( function( json ) {
						buildRecipesList( json.content );
						deferred.resolve();
					})
				$( "#recipes" ).fadeIn( "fast" );
				return deferred.promise();
			};

			var buildRecipesList = function( collection ) {
				$("#list li").remove();
				$.each($(collection), function(idx, recipe) {
					var url = "";
					$.each(recipe.links, function(idx, link) {
						if (link.rel == "self") {
							url = link.href;
						}
					});
					a = $("<a>").attr("href", "#");
					a.text(recipe.title);
					a.click(function() {
						$("#recipe").hide();
						showRecipeCallback( url );
					});
					li = $("<li>").append(a);
					li.appendTo("#list");
				});
				if ($("#list li").length < pageSize) {
					$("#nextPage").fadeOut("fast");
				} else {
					$("#nextPage").show();
				}
			};

			return {
				init: init,
				show: show
			}
		})();
		
		var recipe = (function() {
			
			var init = function() {
				$( "#recipe" ).hide();
				$( "#choice" ).show();
			};
			
			var show = function( url ) {
				var deferred = $.Deferred();
				$.getJSON( url )
					.done( function(recipe) {
						buildRecipe( recipe );
						$.each( recipe.links, function(idx, link) {
							if ( link.rel == "ingredients" ) {
								getIngredients( link.href );
							}
						});
						$( "#choice" ).hide();
						$( "#recipe" ).fadeIn();
						$( "#recipe #resourceId" ).hide();
						deferred.resolve();
					})
					.fail(function(xhr, status, error) {
						var err = "Request Failed: " + status + ", " + xhr.status + ", " + error;
						console.log(err);
						deferred.reject( err );
					})
				return deferred.promise();
			};
			
			var hide = function() {
				$( "#recipe" ).hide();
			};
				
			var buildRecipe = function( recipe ) {

				// Rezept-View mit Daten abfüllen        			
				$( "#recipe #title" ).html( recipe.title );
				$( "#recipe #preamble" ).html( recipe.preamble );
				$( "#recipe #noofperson" ).text( recipe.noOfPerson );
				$( "#recipe #preparation" ).html( recipe.preparation );
				$( "#recipe #rating" ).html( recipe.rating );
				$( "#recipe #adding" ).text( formatDate( recipe.addingDate ) );
				$( "#recipe #update" ).text( formatDate( recipe.editingDate ) );
				$( "#recipe #tags span" ).remove();
				$.each( recipe.tags, function( idx, tag ) {
					$( "#recipe #tags" ).append( "<span><span class='w3-tag w3-teal'>" + tag + "</span>&nbsp;</span>" );
				});
				$( "#recipe #resourceId" ).text( recipe.uuid );
			};

			var getIngredients = function( url ) {
				$.getJSON( url )
					.done(function(ingredients) {
						buildIngredients(ingredients);
					}).fail(function(xhr, status, error) {
						var err = "Request Failed: " + status + ", " + xhr.status + ", " + error;
						console.log(err);
					})
			};

			var buildIngredients = function( ingredients ) {
				ingredients.sort(function(a, b) {
					var a1 = a.description, b1 = b.description;
					if (a1 == b1)
						return 0;
					return a1 > b1 ? 1 : -1;
				});
				$( "#ingredients table" ).remove();
				ingredientsTable = $( "<table>" ).attr({
					"class" : "w3-table w3-bordered",
					"style" : "width: 50%;"
				});
				$( "#ingredients" ).append( ingredientsTable )
				$.each(ingredients, function(idx, ingredient) {
					var tr = $("<tr>");
					var tdPortion = $("<td>").attr({
						"style" : "white-space:nowrap;"
					});
					if (ingredient.portion) {
						tdPortion.text(ingredient.portion);
					}
					var tdDescr = $("<td>");
					if (ingredient.comment) {
						tdDescr.html(ingredient.description + " " + ingredient.comment);
					} else {
						tdDescr.html(ingredient.description);
					}
					var tdSpace = $("<td>").html("&nbsp;");
					tr.append(tdPortion);
					tr.append(tdSpace);
					tr.append(tdDescr);
					$("#ingredients table").append(tr);
				});
			};
		
			var getResourceId = function() {
				return $( "#recipe #resourceId" ).text();
			};
			
			var formatDate = function( number ) {
				var $myDate = new Date(number);
				return $myDate.toLocaleString();
			};
			
			return {
				init: init,
				show: show,
				hide: hide,
				getResourceId: getResourceId
			}
		})();
		
		var recipeEdit = (function() {
			
			var recipesUrl;
			var token;
			
			var init = function( url, tokenProvider ) {
				recipesUrl = url;
				token = tokenProvider;
				hide();
				$( "#recipe-edit #addIngredient").click( function() { addIngredient(); })
				initCkEditor();
	        	$( "#recipe-edit #recipeForm" ).submit( function( event ) {
	        		event.preventDefault();
	        		console.log( "save Recipe" );
	        		save();
	        		return false;
	        	});
			};
			
			var hide = function() {
				$( "#recipeEdit" ).hide();
			};
			
			var initTagEditor = function() {
				var deferred = $.Deferred();
		
				$( "#recipe-edit #tags" ).tagEditor( "destroy" );
				$( "#recipe-edit textarea[name='tags']" ).val( null );
			
				$.getJSON( recipesUrl + "/" + "tags" )
					.done( function( tags ) { 
						$allTags = tags;
						console.log( "Tags: " + $allTags )
						$( "#recipe-edit #tags" ).tagEditor( { 
							autocomplete: {
					        	source: $allTags,
					        	delay: 0,
				        		position: { collision: "flip" }
					    	},
					    	forceLowercase: true
					    } );
		 				deferred.resolve();
					})
					.fail( function( xhr, status, error ) {
		  				var err = "Request Failed: " + status + ", " + xhr.status + ", " + error;
						console.log(err);
		 				deferred.reject( err );
	 				})
	 			return deferred.promise();
			};
			
			var newRecipe = function() {
				$( "#recipe-edit #status" ).hide();
				$( "#choice" ).hide();
				initTagEditor().then( function() {
						resetForm(); 
						$( "#recipeEdit" ).fadeIn(); 
					} );
			};
			
			var show = function( resourceId ) {
				$( "#recipe-edit #status" ).hide();
				initTagEditor().then( processRecipe( resourceId )
					.then( function() { $( "#recipeEdit" ).fadeIn(); } ) )
					.fail( function( message ) { dialogMessage.show( message )
				});
			};
			
			var resetForm = function() {
				$( "#recipe-edit #uuid").val( null );
				$( "#recipe-edit #title" ).val( null );
				CKEDITOR.instances['editPreamble'].setData( "" );
				$( "#recipe-edit #noOfPerson" ).val( null );
				CKEDITOR.instances['editPreparation'].setData( "" );
	        	$( "#recipe-edit input[name='rating'][value='5']" ).prop( "checked", true );
				$( "#recipe-edit #ingredients li" ).remove();
			}
			
			var processRecipe = function( resourceId ) {
				resetForm();
				var deferred = $.Deferred();
				var $ingredientsUrl;
				var $recipeUrl = recipesUrl + "/" + resourceId;
				$.getJSON( $recipeUrl )
					.then( function( recipe ) { 
						$.each(recipe.links, function( idx, link ) {
							if (link.rel == "ingredients") {
								$ingredientsUrl = link.href;
							}
						});
						$recipeObject = recipe;
						buildRecipe( recipe );
						return recipe.tags;
					})
	 				.then( function( tags ) {
	 					getIngredients( $ingredientsUrl );
	 					buildTagEditor( tags )
						deferred.resolve();
	 				})
					.fail( function( xhr, status, error ) {
		  				var err = "Request Failed: " + status + ", " + xhr.status + ", " + error;
						console.log( err );
		 				deferred.reject( err );
	 				})
	 			return deferred.promise();
			};
			
			var buildTagEditor = function( tags ) {
				$.each( tags, function( idx, tag ) {
					$( "#recipe-edit #tags" ).tagEditor( "addTag", tag, true );
	        	});
			};
					
			var getIngredients = function( url ) {
				$( "#recipe-edit #ingredients li" ).remove();
				$.getJSON( url )
					.done( function( ingredients ) {
						$.each( ingredients, function( idx, ingredient ) {
							buildIngredient( ingredient );	
			        	});
					})
					.fail( function( xhr, status, error ) {
	   				    var err = "Request Failed: " + status + ", " + xhr.status + ", " + error;
						console.log( err );
	  				})
			};
		
			var buildIngredient = function( ingredient ) {
				
				li = $( "<li class='ingredient'>" );
				li.attr({
					"id": ingredient.resourceId
				});
				li.appendTo( $("#recipe-edit #ingredients" ) );
				
				rowDiv = $( "<div class='w3-row-padding'>" );
				rowDiv.appendTo( li );
				
				/* ---------- Quantity */
				colQuantity = $( "<div class='w3-col s3'>" );
				colQuantity.appendTo( rowDiv );
				inputQuantity = $( "<input class='w3-input' type='text' min='1' max='255' name='portion'>" );
				if ( ingredient.portion ) {
					inputQuantity.val( ingredient.portion );
				}
				inputQuantity.appendTo( colQuantity );
				labelQuantity = $("<label class='w3-text-gray'>");
				labelQuantity.text( "Quantity" );
				labelQuantity.appendTo( colQuantity );
				
				/* ---------- Description */
				colDescription = $( "<div class='w3-col s4'>" );
				colDescription.appendTo( rowDiv );
				inputDescription = $( "<input class='w3-input' type='text' min='1' max='255' required name='description'>" );
				if ( ingredient.description ) {
					inputDescription.val( ingredient.description );
				}
				inputDescription.appendTo( colDescription );
				labelDescription = $( "<label class='w3-text-gray'>" );
				labelDescription.text( "Description" );
				labelDescription.appendTo( colDescription );
				
				/* ---------- Annotation */
				colAnnotation = $( "<div class='w3-col s4'>" );
				colAnnotation.appendTo( rowDiv );
				inputAnnotation = $( "<input class='w3-input' type='text' min='1' max='255' name='comment'>" );
				if ( ingredient.comment ) {
					inputAnnotation.val( ingredient.comment );
				}
				inputAnnotation.appendTo( colAnnotation );
				labelAnnotation = $( "<label class='w3-text-gray'>" );
				labelAnnotation.text( "Annotation" );
				labelAnnotation.appendTo( colAnnotation );
				
				/* ---------- Remove-Button */
				colRemove = $("<div class='w3-col s1'>");
				colRemove.appendTo( rowDiv );
				spanRemove = $("<span class='w3-button w3-white w3-xlarge w3-right w3-light-gray'>&times;</span>")
				spanRemove.appendTo( colRemove );
				spanRemove.click( function() {
					if ( ingredient.resourceId ) {
						$( "#recipe-edit .ingredient#" + ingredient.resourceId ).fadeOut( "fast" );
					} else {
						$( this ).closest( $( ".ingredient" ) ).fadeOut( "fast", function() {
							$( this ).closest( $( ".ingredient" ) ).remove();
						});
					}	
				})
			};
		
			var buildRecipe = function( recipe ) {
				$( "#recipe-edit #uuid").val( recipe.uuid );
				$( "#recipe-edit #title" ).val( recipe.title );
				CKEDITOR.instances['editPreamble'].setData( recipe.preamble );
				$( "#recipe-edit #noOfPerson" ).val( recipe.noOfPerson );
				CKEDITOR.instances['editPreparation'].setData( recipe.preparation );
	        	$( "#recipe-edit input[name='rating'][value='" + recipe.rating + "']" ).prop( "checked", true );
			};
			
			
			var addIngredient = function() {
				ingredient = {};
				buildIngredient( ingredient )
			};
		
			var save = function() {
				updateRecipe().then( function() {
					$( "#recipe-edit #status" ).fadeIn( "fast");
	  				$( "#recipe-edit #status" ).text( "Rezept gespeichert." );
	  				setTimeout( function() {
	  					$( "#recipe-edit #status" ).fadeOut( "slow" );
	  				}, 1000);
				})
			};
		
			var updateRecipe = function() {
				var deferred = $.Deferred();
				$recipe = {};
				$recipe.uuid = $( "#recipe-edit input[name='uuid']" ).val();
				if ( !$recipe.uuid ) {
					$recipe.uuid = null;
				}
				$recipe.title = $( "#recipe-edit input[name='title']" ).val().trim();
				$recipe.preamble = CKEDITOR.instances['editPreamble'].getData();
				if ( !$recipe.preamble ) {
					$recipe.preamble = null;
				}
				$recipe.noOfPerson = $( "#recipe-edit input[name='noOfPerson']" ).val().trim();
				$recipe.preparation = CKEDITOR.instances['editPreparation'].getData();
				$recipe.rating = $( "#recipe-edit input[name='rating']:checked" ).val().trim();
				$recipe.tags = $( "#recipe-edit #tags" ).tagEditor('getTags')[0].tags;
				
				$json = JSON.stringify($recipe);
				console.log( "Recipe : " + $json );
				
				if ( $recipe.uuid ) {
					// bestehendes Rezept
					$.ajax({
						    url: $recipesUrl + "/" + $recipe.uuid,
						    method: "PUT",
						    contentType: "application/json; charset=UTF-8",
						    data: $json,
							headers: { "Authorization": "Bearer " + token() }
					})
					.fail( function( xhr, status, error ) {
		   				    var err = "Request Failed: " + status + ", " + xhr.status + ", " + error;
							console.log( err );
		  					deferred.reject( err );
		  			})
		  			.then( function() {
		  				updateIngredients( $recipe.uuid )
			  				.then( function() {
			  					show( $recipe.uuid ); // Daten in der View aktualisieren
			  					deferred.resolve(); 
			  				})
			  				.fail( function( xhr, status, error ) {
		   				    	var err = "Request Failed: " + status + ", " + xhr.status + ", " + error;
								console.log( err );
		  						deferred.reject( err );
		  					});
		  				
		  			})
	  			} else {
	  				// neues Rezept
	  				$.ajax({
						    url: $recipesUrl,
						    method: "POST",
						    contentType: "application/json; charset=UTF-8",
						    data: $json,
							headers: { "Authorization": "Bearer " + token() }
					})
					.fail( function( xhr, status, error ) {
		   				    var err = "Request Failed: " + status + ", " + xhr.status + ", " + error;
							console.log( err );
							deferred.reject( err );
		  			})
		  			.then( function( data, status, xhr ) {
		  				$location = xhr.getResponseHeader('Location');
		  				$uuid = $location.substr( $location.lastIndexOf("/") + 1 );
		  				updateIngredients( $uuid )
			  				.then( function() {
			  					show( $uuid ); // Daten in der View aktualisieren
			  					deferred.resolve(); 
			  				})
		  					.fail( function( xhr, status, error ) {
		   				    	var err = "Request Failed: " + status + ", " + xhr.status + ", " + error;
								console.log( err );
		  						deferred.reject( err );
		  					});
		  			})
	  			}
				
				return deferred.promise();
			};
		
			var updateIngredients = function( recipeId ) {
				var deferred = $.Deferred();
				$( "#recipe-edit .ingredient" ).each( function( index, li ) {
					$( "body" ).queue( function() {
						$ingredient = {};
						$ingredient.resourceId = $( li ).attr( "id" );
						$ingredient.portion = $( li ).find( "input[name='portion']" ).val();
						if ( !$ingredient.portion ) {
							$ingredient.portion = null;
						}
						$ingredient.description = $( li ).find( "input[name='description']" ).val();
						$ingredient.comment = $( li ).find( "input[name='comment']" ).val();
						if ( !$ingredient.comment ) {
							$ingredient.comment = null;
						}
					
						updateIngredient( $( li ).is(":hidden"), $ingredient, recipeId );
					})
				});
				$( "body" ).promise().done( deferred.resolve );
				return deferred.promise();
			};
		
			var updateIngredient = function( isHidden, ingredient, recipeId ) {
				var $url = $recipesUrl + "/" + recipeId + "/ingredients/";
				
				if ( isHidden ) {
					console.log( "Ingredient for delete : " + JSON.stringify( ingredient ) );
					$.ajax({
						type: "DELETE",
						url: $url + ingredient.resourceId,
						headers: { "Authorization": "Bearer " + token() },
						success: function() { $( "body" ).dequeue(); }
					});
				} else {
					if ( $ingredient.resourceId == undefined ) {
						console.log( "new Ingredient : " + JSON.stringify( ingredient ) );
						$.ajax({
							type: "POST",
							url: $url,
							data: JSON.stringify( ingredient ),
							contentType: "application/json; charset=UTF-8",
							headers: { "Authorization": "Bearer " + token() },
							success: function() { $( "body" ).dequeue(); }
						});
					} else {
						console.log( "exists Ingredient : " + JSON.stringify( ingredient ) );
						$.ajax({
							type: "PUT",
							url: $url + ingredient.resourceId,
							data: JSON.stringify( $ingredient ),
							contentType: "application/json; charset=UTF-8",
							headers: { "Authorization": "Bearer " + token() },
							success: function() { $( "body" ).dequeue(); }
						});
					}
				}
			};
			
			var initCkEditor = function() {
				// Toolbarkonfigurator: http://ckeditor.com/latest/samples/toolbarconfigurator/index.html
				// entities: false -> Umlaute nicht codieren
				CKEDITOR.replace( "editPreamble", {
				    contentsCss: "resources/ckEditorContents.css",
				    entities: false,
				    resize_enabled: true,
				    language: "de",
				    toolbarGroups: [
						{ name: 'clipboard', groups: [ 'clipboard', 'undo' ] },
						{ name: 'editing', groups: [ 'find', 'selection', 'spellchecker', 'editing' ] },
						{ name: 'forms', groups: [ 'forms' ] },
						'/',
						{ name: 'basicstyles', groups: [ 'basicstyles', 'cleanup' ] },
						{ name: 'paragraph', groups: [ 'list', 'indent', 'blocks', 'align', 'bidi', 'paragraph' ] },
						{ name: 'document', groups: [ 'document', 'doctools', 'mode' ] },
						{ name: 'links', groups: [ 'links' ] },
						{ name: 'insert', groups: [ 'insert' ] },
						'/',
						{ name: 'styles', groups: [ 'styles' ] },
						{ name: 'colors', groups: [ 'colors' ] },
						{ name: 'tools', groups: [ 'tools' ] },
						{ name: 'others', groups: [ 'others' ] },
						{ name: 'about', groups: [ 'about' ] }
					],	
					removeButtons: 'Save,Templates,Cut,Undo,Copy,Paste,PasteText,PasteFromWord,Preview,NewPage,Print,Redo,Replace,Find,SelectAll,Scayt,Form,Radio,Checkbox,TextField,Textarea,Select,Button,ImageButton,HiddenField,Subscript,Superscript,CopyFormatting,RemoveFormat,Blockquote,CreateDiv,JustifyLeft,JustifyCenter,JustifyBlock,BidiLtr,BidiRtl,Language,Anchor,Unlink,Link,Image,Flash,Table,HorizontalRule,Smiley,SpecialChar,PageBreak,Iframe,FontSize,Font,Format,Styles,TextColor,BGColor,Maximize,ShowBlocks,About,JustifyRight'		
				});
				CKEDITOR.replace( "editPreparation", {
				    contentsCss: "resources/ckEditorContents.css",
				    entities: false,
				    resize_enabled: true,
				    language: "de",
				    toolbarGroups: [
						{ name: 'clipboard', groups: [ 'clipboard', 'undo' ] },
						{ name: 'editing', groups: [ 'find', 'selection', 'spellchecker', 'editing' ] },
						{ name: 'forms', groups: [ 'forms' ] },
						'/',
						{ name: 'basicstyles', groups: [ 'basicstyles', 'cleanup' ] },
						{ name: 'paragraph', groups: [ 'list', 'indent', 'blocks', 'align', 'bidi', 'paragraph' ] },
						{ name: 'document', groups: [ 'document', 'doctools', 'mode' ] },
						{ name: 'links', groups: [ 'links' ] },
						{ name: 'insert', groups: [ 'insert' ] },
						'/',
						{ name: 'styles', groups: [ 'styles' ] },
						{ name: 'colors', groups: [ 'colors' ] },
						{ name: 'tools', groups: [ 'tools' ] },
						{ name: 'others', groups: [ 'others' ] },
						{ name: 'about', groups: [ 'about' ] }
					],	
					removeButtons: 'Save,Templates,Cut,Undo,Copy,Paste,PasteText,PasteFromWord,Preview,NewPage,Print,Redo,Replace,Find,SelectAll,Scayt,Form,Radio,Checkbox,TextField,Textarea,Select,Button,ImageButton,HiddenField,Subscript,Superscript,CopyFormatting,RemoveFormat,Blockquote,CreateDiv,JustifyLeft,JustifyCenter,JustifyBlock,BidiLtr,BidiRtl,Language,Anchor,Unlink,Link,Image,Flash,Table,HorizontalRule,Smiley,SpecialChar,PageBreak,Iframe,FontSize,Font,Format,Styles,TextColor,BGColor,Maximize,ShowBlocks,About,JustifyRight'		
				});
			};
			
			return {
				show: show,
				hide: hide, 
				init: init,
				newRecipe: newRecipe
			}
		
		})();
		
		var cookbook = (function() {

			var init = function() {
				recipes.init( $recipesUrl, showRecipe );
				recipeEdit.init( $recipesUrl, tokenProvider );
				recipe.init();
				recipes.show().fail( errorHandler );
				user.change( function( token ) {
					if ( token ) {
						$( "#editAndDelete" ).show();
						$( "#addRecipe").show();
						$( "#backupDatabase" ).show();
					} else {
						$( "#editAndDelete" ).hide();
						$( "#addRecipe").hide();
						$( "#backupDatabase" ).hide();
					}
				});
				$( "#editAndDelete" ).hide();
				$( "#addRecipe").hide();
				$( "#backupDatabase" ).hide();
				$( "#addRecipe").click( function() {
					newRecipe();
				});
				$( "#editAndDelete #edit" ).click( function() {
					editRecipe();
				});
				
				$( "#editAndDelete #delete" ).click( function() {
					dialogDelete.show();
				});
				$( "#backupDatabase" ).click( function() {
					backup().fail( errorHandler );
				});
				$( "#about" ).click( function() {
					about().fail( errorHandler );
				});
			};
			
			var showRecipe = function( url ) {
				recipeEdit.hide();
				recipe.show( url ).fail( errorHandler );
			};

			var errorHandler = function( message ) {
				dialogMessage.show( "Error\n" + message );
			};
			
			var tokenProvider = function() {
				return user.getToken();
			};

			var editRecipe = function() {
				recipe.hide();
				recipeEdit.show( recipe.getResourceId() );
			};
			
			var newRecipe = function() {
				recipe.hide();
				recipeEdit.newRecipe();
			}
			
			var deleteRecipe = function() {
				var deferred = $.Deferred();
				var $recourceId = recipe.getResourceId();
				var $token = user.getToken();
				$.ajax({
					url : $recipesUrl + "/" + $recourceId,
					method : "DELETE",
					headers: { "Authorization": "Bearer " + $token }
				})
				.fail( function( xhr, status, error ) {
					var err = "Request Failed: " + status + ", " + xhr.status + ", " + error;
					console.log( err );
					deferred.reject( err );
				})
				.then(function() {
					deferred.resolve();
				})
				return deferred.promise();
			};
			
			var backup = function( ) {
				var deferred = $.Deferred();
				var $backupUrl = $recipesUrl + "/backup";
				var $token = user.getToken();
				$.ajax({
				    url: $backupUrl,
				    method: "POST",
				    contentType: "application/json; charset=UTF-8",
					headers: { "Authorization": "Bearer " + $token }
				})
				.fail( function(xhr, status, error) {
					var err = "Request Failed: " + status + ", " + xhr.status + ", " + error;
					console.log( err );
					deferred.reject( err );
				})
				.then( function( json ) {
					var $message = "Backup erstellt. Backup-Path: '" + json.backuppath + "'";
					dialogMessage.show( $message );
					deferred.resolve();
				})
				return deferred.promise();
			};
			
			var about = function( ) {
				var deferred = $.Deferred();
				$.getJSON( $rootUrl + "/infos" )
					.done( function( about ) {
						var $message = "Client-Release: ${release} <br>";
				        $message += "Server<br>";
				        $message += "Applikations-Release: " + about.applicationRelease + "<br>";
				        $message += "Spring-Version: " + about.springVersion + "<br>";
				        $message += "Spring-Security-Version: " + about.springSecurityVersion + "<br>";
				        $message += "Datenbank-Version (Derby): " + about.derbyVersion + "<br>";
				        dialogMessage.show( $message );
						deferred.resolve();
					})
					.fail(function(xhr, status, error) {
						var err = "Request Failed: " + status + ", " + xhr.status + ", " + error;
						console.log(err);
						deferred.reject( err );
					})
				return deferred.promise();
			};
			
			return {
				init: init,
				deleteRecipe: deleteRecipe,
				backup: backup,
				about: about
			}
			
		})();

		$(function() {
			cookbook.init();
		});
		$(document).ajaxStart( function() {
			$( "#loading" ).show();
			$( "footer" ).hide();
		});
		$(document).ajaxStop( function() {
			$( "#loading" ).hide();
			$( "footer" ).show();
		});
	</script>
</body>
</html>