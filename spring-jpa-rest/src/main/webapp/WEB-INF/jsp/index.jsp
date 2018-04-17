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
		/* tag-editor */
		.tag-editor-tag {color:#fff !important; background-color:#77a3ca !important}	/* w3-theme-d1 */
		.tag-editor-spacer {color:#f8fbfc !important; background-color:#f8fbfc !important} /* w3-theme-l5 */
		/* delete icon */
		.tag-editor .tag-editor-delete {color:#fff !important; background-color:#77a3ca !important}	/* w3-theme-d1 */
		.tag-editor .tag-editor-delete i:before { color:#fff !important; }
		.tag-editor .tag-editor-delete:hover i:before { color: #f44336 !important; }	/* w3-red */
		 
		/* w3.css theme (siehe https://www.w3schools.com/w3css/w3css_color_generator.asp) */
		.w3-theme-l5 {color:#000 !important; background-color:#f8fbfc !important}
		.w3-theme-l4 {color:#000 !important; background-color:#e9f0f6 !important}
		.w3-theme-l3 {color:#000 !important; background-color:#d3e1ee !important}
		.w3-theme-l2 {color:#000 !important; background-color:#bdd2e5 !important}
		.w3-theme-l1 {color:#000 !important; background-color:#a7c4dd !important}
		.w3-theme-d1 {color:#fff !important; background-color:#77a3ca !important}
		.w3-theme-d2 {color:#fff !important; background-color:#5d92c0 !important}
		.w3-theme-d3 {color:#fff !important; background-color:#4681b4 !important}
		.w3-theme-d4 {color:#fff !important; background-color:#3c6e9a !important}
		.w3-theme-d5 {color:#fff !important; background-color:#325c81 !important}
		
		.w3-theme-light {color:#000 !important; background-color:#f8fbfc !important}
		.w3-theme-dark {color:#fff !important; background-color:#325c81 !important}
		.w3-theme-action {color:#fff !important; background-color:#325c81 !important}
		
		.w3-theme {color:#000 !important; background-color:#92b6d5 !important}
		.w3-text-theme {color:#92b6d5 !important}
		.w3-border-theme {border-color:#92b6d5 !important}
		
		.w3-hover-theme:hover {color:#000 !important; background-color:#92b6d5 !important}
		.w3-hover-text-theme {color:#92b6d5 !important}
		.w3-hover-border-theme:hover {border-color:#92b6d5 !important}
	</style>
	<title>Kochbuch chez René</title>
</head>
<body class="w3-theme-l5">
	<!-- Sidebar -->
	<div class="w3-sidebar w3-bar-block w3-card-2 w3-animate-left w3-theme-l4" style="display:none;left:0;" id="mySidebar">
	  <button class="w3-bar-item w3-button w3-red" onclick="w3_close()">Close</button>
	  <button class="w3-button w3-bar-item w3-theme-d5" onclick="dialogLogin.show()">Login</button>
	  <button id="addRecipe" class="w3-button w3-bar-item w3-theme-d5" title="neues Rezept erfassen">Neues Rezept</button>
      <button id="backupDatabase" class="w3-button w3-bar-item w3-theme-d5" title="Backup Database">Backup Database</button>
      <button id="about" class="w3-button w3-bar-item w3-theme-d5" title="About">About</button>
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
	<header class="w3-bar w3-theme-d5 w3-xxlarge">
	 	<button class="w3-bar-item w3-button w3-theme-d5" onclick="w3_open()">&#9776;</button>
  		<span class="w3-bar-item">Kochbuch chez René</span>
	</header> 

	<div class="w3-row">
		<div class="w3-third w3-container">
			<h3>Liste aller Rezepte <span id="loading"><i class="fa fa-refresh"></i></span></h3>
			<div id="recipes">
				<ul class="w3-ul w3-hoverable w3-large" id="list"></ul>
				<div class="w3-bar w3-center">
					<button id="prevPage" class="w3-button w3-xxlarge" title="previous">&laquo;</button>
					<button id="nextPage" class="w3-button w3-xxlarge" title="next">&raquo;</button>
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
			    <p>Rating <span class="w3-badge w3-theme-d4" id="rating"></span></p>
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
				    		<button id="edit" class="w3-button w3-theme-d4" type="submit">Editieren</button>&nbsp;
				    		<button id="delete" class="w3-button w3-theme-d4">Löschen</button>
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
							<label class="w3-text-theme">Title</label>
						</div>
						<div class="w3-panel">
							<textarea name="preamble" id="editPreamble"></textarea>
							<label class="w3-text-theme">Preamble</label>
						</div>
						<div class="w3-panel">
							<label class="w3-text-theme">Zutaten für</label>
							<input class="w3-input" style="width:25%" type="text" min="1" max="10" required id="noOfPerson" name="noOfPerson">
							<label class="w3-text-theme">Personen</label>
						</div>
						<div class="w3-panel">
							<ul class="w3-ul w3-theme-l5" id="ingredients">
								<li><h3>Zutaten</h3></li>
							</ul>
						</div>
						<div class="w3-panel">
							<button class="w3-button w3-circle w3-theme-d4" id="addIngredient" title="Zutat hinzufügen">+</button>
						</div>
						<div class="w3-panel">
							<textarea name="preparation" id="editPreparation"></textarea>
							<label class="w3-text-theme">Zubereitung</label>
						</div>
						<div class="w3-panel">
							<textarea name="tags" id="tags"></textarea>
							<label class="w3-text-theme">Stichworte</label>
						</div>
						<div class="w3-panel">
							<div class="w3-row-padding">
								<input class="w3-radio" type="radio" name="rating" value="1" checked><label><span class="w3-badge w3-theme-d4">1</span></label>
								<input class="w3-radio" type="radio" name="rating" value="2" checked><label><span class="w3-badge w3-theme-d4">2</span></label>
								<input class="w3-radio" type="radio" name="rating" value="3" checked><label><span class="w3-badge w3-theme-d4">3</span></label>
								<input class="w3-radio" type="radio" name="rating" value="4" checked><label><span class="w3-badge w3-theme-d4">4</span></label>
								<input class="w3-radio" type="radio" name="rating" value="5" checked><label><span class="w3-badge w3-theme-d4">5</span></label>
							</div>
							<p class="w3-text-theme">Rating</p>
						</div>
						<hr>
						<div class="w3-panel w3-row-padding">
							<div class="w3-col s2"><button class="w3-button w3-theme-d4" type="submit" id="save">Speichern</button></div>
							<div class="w3-col s2"><span class="w3-button w3-green" id="status"></span></div>
							<div class="w3-col s8"></div>
						</div> 
					</form> 
				</div>
		    </div>
		    
		</div>
	</div>
	<p>&nbsp;</p>
	<footer class="w3-container w3-theme-d5">
		<h5>Cookbook</h5>
		<p>by René Anderes</p>
	</footer> 	
	
	<div id="dialogDelete" class="w3-modal">
		<div class="w3-modal-content w3-card-4 w3-animate-zoom" style="max-width:400px">
			<header class="w3-container w3-theme-d5">
				<span onclick="dialogDelete.cancel();" class="w3-button w3-theme-d4 w3-xlarge w3-display-topright" title="Close Dialog">&times;</span>
				<h2>Confirmation Required</h2>
			</header>
			<div class="w3-container">
				<p>Das Rezept wirklich löschen?</p>
			</div>
			<div class="w3-container w3-theme-l4 w3-padding">
				<div class="w3-bar w3-right-align">
					<button class="w3-button w3-theme-d4" onclick="dialogDelete.confirm();">Ja</button>
					<button class="w3-button w3-theme-d4" onclick="dialogDelete.cancel();">Nein</button>
				</div>
			</div>
		</div>
	</div>
	
	<div id="dialogMessage" class="w3-modal">
		<div class="w3-modal-content w3-card-4 w3-animate-zoom" style="max-width:400px">
			<header class="w3-container w3-theme-d5">
				<span onclick="dialogMessage.cancel();" class="w3-button w3-theme-d5 w3-xlarge w3-display-topright" title="Close Dialog">&times;</span>
				<h2>Meldung</h2>
			</header>
			<div class="w3-container">
				<p id="messageText"></p>
			</div>
			<div class="w3-container w3-theme-l4 w3-padding">
				<div class="w3-bar w3-right-align">
					<button class="w3-button w3-theme-d4" onclick="dialogMessage.cancel();">OK</button>
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
					<button class="w3-button w3-block w3-theme-d4 w3-section w3-padding" type="submit">Login</button>
				</div>
			</form>

			<div class="w3-container w3-border-top w3-padding-16 w3-theme-l4">
				<button onclick="dialogLogin.cancel();" type="button" class="w3-button w3-theme-d4">Cancel</button>
				<p id="error"></p>
			</div>
		</div>
	</div>
	<script>
		const $recipesUrl = cookbookAPI.getRecipesRootUrl();
		const $tokenUrl = "/spring-security-jwt-server/users/token";

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
			var last = false;
			var first = true;
			var showRecipeCallback;
			
			var init = function( recipesUrl, recipeHandler ) {
				url = recipesUrl;
				showRecipeCallback = recipeHandler;
				$( "#recipes" ).hide();
				$( "#nextPage" ).on( "click", function( e ) {
					e.preventDefault();
					if ( !last ) {
						pageNo++;
						$( "#recipes" ).hide();
						show();
					}
				});
				$( "#prevPage" ).on( "click", function( e ) {
					e.preventDefault();
					if ( !first ) {
						pageNo--;
						$( "#recipes" ).hide();
						show();
					}
				});
			};
		
			var show = function() {
				var deferred = $.Deferred();
				cookbookAPI.load( cookbookAPI.getRecipesUrl( pageNo, pageSize ) )
					.then( function( json ) {
						buildRecipesList( json.content );
						last = json.last;
						first = json.first;
						$( "#recipes" ).fadeIn( "slow" );
						$( "#nextPage" ).prop( "disabled", last );
						$( "#prevPage" ).prop( "disabled", first );
						deferred.resolve();
					})
					.fail( function( error ) {
						deferred.reject( error );
					})
				return deferred.promise();
			};

			var buildRecipesList = function( collection ) {
				$("#list li").remove();
				$.each($(collection), function(idx, recipe) {
					var links = $.grep( recipe.links, function( link ) {
						return link.rel == "self";
					});
					var url = links[0].href;
					
					a = $("<a>").attr("href", "#");
					a.text(recipe.title);
					a.click(function() {
						$("#recipe").hide();
						showRecipeCallback( url );
					});
					li = $("<li>").append(a);
					li.appendTo("#list");
				});
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
				cookbookAPI.load( url )
					.then( function(recipe) {
						buildRecipe( recipe );
						var links = $.grep( recipe.links, function( link ) {
							return link.rel == "ingredients";
						});
						getIngredients( links[0].href );
						$( "#choice" ).hide();
						$( "#recipe" ).fadeIn();
						$( "#recipe #resourceId" ).hide();
						deferred.resolve();
					})
					.fail( function( error ) {
						deferred.reject( error );
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
					$( "#recipe #tags" ).append( "<span><span class='w3-tag w3-theme-d4'>" + tag + "</span>&nbsp;</span>" );
				});
				$( "#recipe #resourceId" ).text( recipe.uuid );
			};

			var getIngredients = function( url ) {
				cookbookAPI.load( url )
					.then( function(ingredients ) {
						buildIngredients(ingredients);
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
			
				cookbookAPI.load( cookbookAPI.getTagsUrl() )
					.then( function( tags ) { 
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
					.fail( function( error ) {
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
				$( "#recipeEdit" ).hide();
				$( "#recipe-edit #status" ).hide();
				initTagEditor().then( 
						processRecipe( resourceId ).then( function() { 
								$( "#recipeEdit" ).fadeIn(); 
							})
					).fail( function( message ) { 
						dialogMessage.show( message ) 
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
				cookbookAPI.load( cookbookAPI.getRecipeUrl( resourceId ) )
					.then( function( recipe ) { 
	 					loadAndBuildIngredients( recipe );
						mapRecipeToHtmlForm( recipe );
						return recipe.tags;
					})
	 				.then( function( tags ) {
	 					buildTagEditor( tags )
						deferred.resolve();
	 				})
					.fail( function( error ) {
		 				deferred.reject( error );
	 				})
	 			return deferred.promise();
			};
			
			var buildTagEditor = function( tags ) {
				$.each( tags, function( idx, tag ) {
					$( "#recipe-edit #tags" ).tagEditor( "addTag", tag, true );
	        	});
			};
					
			var loadAndBuildIngredients = function( recipe ) {
				$( "#recipe-edit #ingredients").hide();
				$( "#recipe-edit #ingredients li" ).remove();
				var links = $.grep( recipe.links, function( link ) {
					return link.rel == "ingredients";
				});
				cookbookAPI.load( links[0].href )
					.then( function( ingredients ) {
						$.each( ingredients, function( idx, ingredient ) {
							buildIngredient( ingredient );	
			        	});
			        	$( "#recipe-edit #ingredients").fadeIn( "slow" );
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
				labelQuantity = $("<label class='w3-text-theme w3-tiny'>");
				labelQuantity.text( "Menge" );
				labelQuantity.appendTo( colQuantity );
				
				/* ---------- Description */
				colDescription = $( "<div class='w3-col s4'>" );
				colDescription.appendTo( rowDiv );
				inputDescription = $( "<input class='w3-input' type='text' min='1' max='255' required name='description'>" );
				if ( ingredient.description ) {
					inputDescription.val( ingredient.description );
				}
				inputDescription.appendTo( colDescription );
				labelDescription = $( "<label class='w3-text-theme w3-tiny'>" );
				labelDescription.text( "Beschreibung" );
				labelDescription.appendTo( colDescription );
				
				/* ---------- Annotation */
				colAnnotation = $( "<div class='w3-col s4'>" );
				colAnnotation.appendTo( rowDiv );
				inputAnnotation = $( "<input class='w3-input' type='text' min='1' max='255' name='comment'>" );
				if ( ingredient.comment ) {
					inputAnnotation.val( ingredient.comment );
				}
				inputAnnotation.appendTo( colAnnotation );
				labelAnnotation = $( "<label class='w3-text-theme w3-tiny'>" );
				labelAnnotation.text( "Kommentar" );
				labelAnnotation.appendTo( colAnnotation );
				
				/* ---------- Remove-Button */
				colRemove = $("<div class='w3-col s1'>");
				colRemove.appendTo( rowDiv );
				spanRemove = $("<span class='w3-button w3-white w3-xlarge w3-right w3-text-theme'>&times;</span>")
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
		
			var mapRecipeToHtmlForm = function( recipe ) {
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
	  				}, 2000);
				})
			};
		
			var updateRecipe = function() {
				var deferred = $.Deferred();
				$recipe = mapFormDataToRecipe();
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
			  					show( $recipe.uuid ); // Daten in der View aktualisieren, bei neuen Zudaten werde so die ID's gesetzt
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
						$ingredient = mapFormDataToIngredient( li );
						updateIngredient( $( li ).is(":hidden"), $ingredient, recipeId );
					})
				});
				$( "body" ).promise().done( deferred.resolve );
				return deferred.promise();
			};
			
			mapFormDataToRecipe = function() {
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
				return $recipe;
			}
			
			mapFormDataToIngredient = function( li ) {
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
				return $ingredient;
			}
			
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
				var deferred1 = cookbookAPI.load( cookbookAPI.getSpringInfoUrl() );
				var deferred2 = cookbookAPI.load( cookbookAPI.getDatabaseInfoUrl() ) 
				$.when( deferred1, deferred2 )
					.then( function( spring, database ) {
						var $message = "Client-Release: ${release} <p>";
				        $message += "Server<br>";
				        $message += "Spring-Version: " + spring["Spring-Version"] + "<br>";
				        $message += "Database Product-Name: " + database["Product-Name"] + "<br>";
				        $message += "Database Product-Version: " + database["Product-Version"] + "<br>";
				        dialogMessage.show( $message )
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
			$( "footer" ).fadeIn( "slow" );
		});
	</script>
</body>
</html>