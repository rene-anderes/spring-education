<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<c:url var="resources" value="/resources"/>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<script src="${ resources }/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="${ resources }/jquery-ui.min.js"></script>

<link rel="stylesheet" href="${ resources }/jquery-ui.min.css">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<title>Liste</title>
</head>
<body>
	<c:url var="editUrl" value="/edit.html"/>
	<div class="w3-container">
		<h1>Web-Application "spring-jpa-rest"</h1>
		<div class="w3-row-padding">
			<div class="w3-col s6">
				<p>Liste aller Rezepte <span id="loading"><i class="fa fa-refresh"></i></span></p>
				<ul class="w3-ul" style="width:75%" id="list"></ul>
				<hr>
				 <div class="w3-bar">
					<a id="prevPage" class="w3-button">&laquo;</a>
					<a id="nextPage" class="w3-button">&raquo;</a>
				</div> 
				<p><a href="${ editUrl }" class="w3-button w3-circle w3-red">+</a></p>
				<p><button id="loginData" onclick="dialogLogin.show()" class="w3-button w3-red">Login Daten</button>
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
				    <p id="resourceId"></p>
				    <hr>
			    	<form id="editSubmit" action="${ editUrl }">
					    <p>
				    		<button id="editLink" class="w3-button w3-red" type="submit">Editieren</button>&nbsp;
				    		<button id="delete" class="w3-button w3-red">Löschen</button>
				    		<input name="recipeId" type="hidden" />
				    		<input name="token" type="hidden" />
					    </p>
			    	</form>
			    </div>
			</div>
		</div>
		
	</div>
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
				<img src="img_avatar4.png" alt="Avatar" style="width: 30%" class="w3-circle w3-margin-top">
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
		var $loginUrl = $rootUrl + "/users/login";
		var $pageSize = 10;
		var $pageNo = 0;
		var $token = null;

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
					location.reload;
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
				login.getToken( $user, $password )
				.fail( function( message ) {
					$token = null;
					$( "#dialogLogin #error" ).text("Anmeldung nicht erfolgreich: " + message );
				})
				.then( function() {
					$( "#dialogLogin #error" ).text("");
					$( "#dialogLogin" ).hide();
				})
			}
		}
		
		var login = {
		
			getToken: function( user, password ) {
				var deferred = $.Deferred();
				var $loginData = {};
				$loginData.name = user;
				$loginData.password = password;
				$json = JSON.stringify( $loginData );
				$.ajax({
					url : $loginUrl,
					method : "POST",
					contentType: "application/json; charset=UTF-8",
					data: $json,
					dataType: "json",
					success: function( data ){
						$token = data.token;
				    }
				})
				.fail( function(xhr, status, error) {
					var err = "Request Failed: " + status + ", " + xhr.status + ", " + error;
					console.log(err);
					deferred.reject(err);
				})
				.then(function() {
					deferred.resolve();
				});
				return deferred.promise();
			}
		}
		
		var cookbook = {

			init : function() {
				$("#recipe").hide();
				$("#choice").show();
				$("#recipe #resourceId").hide();
				$("#delete").on("click", function(e) {
					e.preventDefault();
					dialogDelete.show();
				});
				$("#nextPage").on("click", function(e) {
					e.preventDefault();
					$pageNo = $pageNo + 1;
					cookbook.showRecipes($recipesUrl);
				});
				$("#prevPage").on("click", function(e) {
					e.preventDefault();
					if ($pageNo > 0) {
						$pageNo--;
						cookbook.showRecipes($recipesUrl);
					}
				});
			},

			deleteRecipe : function() {
				var deferred = $.Deferred();
				var $recourceId = $("#recipe #resourceId").text();
				$.ajax({
					url : $recipesUrl + "/" + $recourceId,
					method : "DELETE",
					headers: { "Authorization": "Bearer " + $token }
				})
				.fail( function(xhr, status, error) {
					var err = "Request Failed: " + status + ", " + xhr.status + ", " + error;
					console.log(err);
					deferred.reject(err);
				})
				.then(function() {
					deferred.resolve();
				})
				return deferred.promise();
			},

			showRecipes : function(url) {
				var deferred = $.Deferred();
				var $completeUrl = url + "?sort=title&page=" + $pageNo + "&size=" + $pageSize;
				$.getJSON($completeUrl)
				.fail( function(xhr, status, error) {
					var err = "Request Failed: " + status + ", " + xhr.status + ", " + error;
					console.log(err);
					deferred.reject(err);
				})
				.then( function(json) {
					cookbook.handleRecipesList(json.content);
					deferred.resolve();
				})
				return deferred.promise();
			},

			handleRecipesList : function(collection) {
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
						cookbook.showRecipe(url);
					});
					li = $("<li>").append(a);
					li.appendTo("#list");
				});
				if ($("#list li").length < $pageSize) {
					$("#nextPage").fadeOut("fast");
				} else {
					$("#nextPage").show();
				}
			},

			showRecipe : function(url) {
				$.getJSON(url).done(function(recipe) {
					cookbook.buildRecipe(recipe);
					$.each(recipe.links, function(idx, link) {
						if (link.rel == "ingredients") {
							cookbook.getIngredients(link.href);
						}
					});
				}).fail(function(xhr, status, error) {
					var err = status + ", " + error;
					console.log("Request Failed: " + err);
				})

			},

			getIngredients : function(url) {
				$.getJSON(url).done(function(ingredients) {
					cookbook.buildIngredients(ingredients);
					$("#choice").hide();
					$("#recipe").fadeIn();
				}).fail(function(xhr, status, error) {
					var err = status + ", " + error;
					console.log("Request Failed: " + err);
				})
			},

			buildRecipe : function(recipe) {

				// Rezept-View mit Daten abfüllen        			
				$("#recipe #title").html(recipe.title);
				$("#recipe #preamble").html(recipe.preamble);
				$("#recipe #noofperson").text(recipe.noOfPerson);
				$("#recipe #preparation").html(recipe.preparation);
				$("#recipe #rating").html(recipe.rating);
				$("#recipe #adding").text( cookbook.formatDate(recipe.addingDate) );
				$("#recipe #update").text( cookbook.formatDate(recipe.editingDate) );
				$("#tags span").remove();
				$.each(recipe.tags, function(idx, tag) {
					$("#tags").append( "<span class='w3-tag'>" + tag + "</span>&nbsp;" );
				});
				$( "#recipe #resourceId" ).text( recipe.uuid );
				$( "#editSubmit input[name='recipeId']" ).val( recipe.uuid );
				
				$( "#editSubmit" ).submit( function( event ) {
					if ( $token ) {
						$( "#editSubmit input[name='token']" ).val( $token );
						return true;
					} else {
						dialogMessage.show( "Bitte zuerst einloggen..." );
						return false;
					}
				});
				$( "#editSubmit" ).click( function() {
					if ( $token ) {
						$( "#editSubmit input[name='token']" ).val( $token );
						deleteDialog.show();
					} else {
						dialogMessage.show( "Bitte zuerst einloggen..." );
					}
				})

			},

			buildIngredients : function(ingredients) {
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
				$( "#ingredients" ).append(ingredientsTable)
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
			},

			checkUrlParameter : function() {
				var $resourceId = cookbook.getRequestParams("id")
				if ($resourceId) {
					$url = $recipesUrl + "/" + $resourceId;
					cookbook.showRecipe($url);
				}
			},

			getRequestParams : function(k) {
				var p = {};
				location.search.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(s,
						k, v) {
					p[k] = v
				})
				return k ? p[k] : p;
			},

			formatDate : function(number) {
				var $myDate = new Date(number);
				return $myDate.toLocaleString();
			}
		};

		$(function() {
			cookbook.init();
			cookbook.showRecipes($recipesUrl)
			.fail(function(message) {
				dialogMessage.show(message)
			})
			.then(cookbook.checkUrlParameter());
		});
		$(document).ajaxStart(function() {
			$("#loading").show();
		});
		$(document).ajaxStop(function() {
			$("#loading").hide();
		});
	</script>
</body>
</html>