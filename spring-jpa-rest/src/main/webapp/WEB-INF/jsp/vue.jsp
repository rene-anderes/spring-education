<%@ page language="java" session="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>

<c:url var="resources" value="/resources"/>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js" type="text/javascript" crossorigin="anonymous"></script>
	<script src="https://code.jquery.com/jquery-3.3.1.min.js" type="text/javascript" crossorigin="anonymous"></script>
	<link rel="stylesheet" href="${ resources }/w3.css">
	<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Proza+Libre">
	<link rel="stylesheet" href="${ resources }/font-awesome-4.7.0/css/font-awesome.min.css">
	<link rel="stylesheet" href="https://www.w3schools.com/lib/w3-theme-dark-grey.css">
	<style>
		html, body, h1, h2, h3, h4, h5, h6 {
		    font-family: "Proza Libre", Verdana, Helvetica, sans-serif;
		}
	</style>
	<title>Cookbook with vue.js</title>
</head>
<body class="w3-theme-d5">
	<script>
		const EventBus = new Vue()
		Object.defineProperties(Vue.prototype, {
			$eventbus: {
				get: function () {
					return EventBus
				}
			}
		});

		Vue.component( 'ingredients-list', {
			data: function() {
				return {
					ingredients: [ ]
				}
			},
			beforeUpdate: function() {
				$( this.$el ).hide();
			},
			updated: function() {
				$( this.$el ).fadeIn( "slow" );
			},
			created: function() { 
				this.$eventbus.$on( 'ingredients-update', function( json ) {
					this.ingredients = json; 
				}.bind(this));
			}
		});
	</script>

	<header class="w3-container w3-theme-l3 w3-margin-bottom">
  		<h1>Kochbuch chez René</h1>
	</header> 
	
	<div id="app" class="w3-row">
		<div class="w3-third w3-container w3-margin-top">
			<div class="w3-container w3-theme">
				<h3>Liste aller Rezepte <span id="loading"><i class="fa fa-refresh"></i></span></h3>
			</div>
			<div id="recipes">
				<ul class="w3-ul w3-hoverable w3-large" id="list">
					<li v-for="recipe in recipes" style="cursor:pointer;" v-on:click="selectRecipe(recipe)">{{ recipe.title }}</li>
				</ul>
				<div class="w3-bar w3-center" id="paging">
					<button v-bind:disabled="pageNo == 0" v-on:click="prevPage" class="w3-button w3-xxlarge" title="previous">&laquo;</button>
					<button v-on:click="nextPage" class="w3-button w3-xxlarge" title="next">&raquo;</button>
				</div>
			</div>
		</div>
		<div class="w3-twothird w3-container">
			<!-- ************************** einzelnes Rezept anzeigen ********************* -->
			<div id="recipe">
				<p v-if="!recipe.uuid">Wähle ein Rezept aus der Liste aus ...</p>
				<div v-else>
				    <h1>{{ recipe.title }}</h1>
				    <p v-html="recipe.preamble"></p>
				    <h3>Zutaten für <span id="noofperson">{{ recipe.noOfPerson }}</span> Personen</h3>
				    <div>
				    	<ingredients-list inline-template>
							<table class="w3-table w3-bordered" style="max-width: 50%">
								<tr v-for="ingredient in ingredients">
									<td>{{ ingredient.portion }}</td>
									<td>{{ ingredient.description }}&nbsp;{{ ingredient.comment ? "(" + ingredient.comment + ")" : "" }}</td>
								</tr>
							</table>
						</ingredients-list>
			    	</div>
			    	<p>&nbsp;</p>
				    <h3>Zubereitung</h3>
				    <p v-html="recipe.preparation"></p>
				    <p>&nbsp;</p>
				    <p>Rating <span class="w3-badge w3-theme-l3" id="rating">{{ recipe.rating }}</span></p>
				    <p class="w3-tiny">
				    	Erfasst: <span id="adding">{{ formatDate(recipe.addingDate) }}</span><br>
				    	Aktualisiert: <span id="update">{{ formatDate(recipe.editingDate) }}</span><br>
				    </p>
					<p class="w3-tiny">Stichworte:&nbsp;<span v-for="tag in recipe.tags"><span class='w3-tag w3-theme-l3'>{{ tag }}</span>&nbsp;</span></p>
				</div>
		    </div>
		</div>
	</div>
	<p>&nbsp;</p>
	<footer class="w3-container w3-center w3-theme-d5" style="display: none;">
		<p>© 1996-2018 René Anderes | www.anderes.org | design and implemented by René Anderes | Powered by w3.css, jQuery &amp; vue.js</p>
	</footer> 

	<script>
		var dataUrl = ( function() {
			var $rootUrl = "/spring-jpa-rest"
			var $recipesUrl = $rootUrl + "/recipes";
			
			var getRecipesUrl = function( pageNo, pageSize ) {
				return $recipesUrl + "?sort=title&page=" + pageNo + "&size=" + pageSize;
			}

			return {
				recipes: getRecipesUrl
			}
		})();

		var dataStorage = ( function() {
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
				load: load
			} 
		})();

		var recipeViewModel = new Vue({
			el: '#recipe',
			data: {
				recipe: {}
			},
			beforeUpdate: function() {
				$( this.$el ).hide();
				dataStorage.load( this.getIngredientsUrl() )
					.then( function( json )	{
						EventBus.$eventbus.$emit( 'ingredients-update', json );
					});
			},
			updated: function() {
				$( this.$el ).fadeIn( "slow" );
			},
			methods: {
				formatDate: function( number ) {
					var $myDate = new Date(number);
					return $myDate.toLocaleString();
				},
				getIngredientsUrl: function() {
					var url = "";
					$.each( this.recipe.links, function(idx, link) {
						if ( link.rel == "ingredients" ) {
							url = link.href ;
						}
					});
					return url;
				}
			}
		});
		
		var recipesViewModel = new Vue({
			el: '#list',
			data: { 
				recipes: {} 
			},
			beforeUpdate: function() {
				$( this.$el ).hide();
			},
			updated: function() {
				$( this.$el ).fadeIn( "slow" );
			},
			methods: {
				selectRecipe: function( recipe ) {
					var url = "";
					$.each(recipe.links, function(idx, link) {
						if (link.rel == "self") {
							url = link.href;
						}
					});
					dataStorage.load( url ).then( function( json ) {
						recipeViewModel.recipe = json;
					})
				}
			}
		});

		var pagingViewModel = new Vue({
			el: '#paging',
			data: {
				pageSize: 10,
				pageNo: 0	
			},
			methods: {
				nextPage: function() {
					this.pageNo++;
					dataStorage.load(dataUrl.recipes(this.pageNo, this.pageSize)).then( function( json ) {
						recipesViewModel.recipes = json.content;
					})
				},
				prevPage: function() {
					if (this.pageNo > 0) {
						this.pageNo--;
						dataStorage.load(dataUrl.recipes(this.pageNo, this.pageSize)).then( function( json ) {
							recipesViewModel.recipes = json.content;
						})
					}
				}
			}
		});
		

		$(function() {
			var url = dataUrl.recipes(pagingViewModel.pageNo, pagingViewModel.pageSize);
			dataStorage.load( url ).then( function( json ) {
				recipesViewModel.recipes = json.content;
				$( "#recipes" ).fadeIn( "slow" );
			})
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