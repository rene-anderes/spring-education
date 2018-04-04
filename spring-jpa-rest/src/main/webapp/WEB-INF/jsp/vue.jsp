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
	<style>
		html, body, h1, h2, h3, h4, h5, h6 {
		    font-family: "Proza Libre", Verdana, Helvetica, sans-serif;
		}
	</style>
	<title>Cookbook with vue.js</title>
</head>
<body>

	<div class="w3-row">
		<div class="w3-third w3-container">
			<h3>Liste aller Rezepte <span id="loading"><i class="fa fa-refresh"></i></span></h3>
			<div id="recipes">
				<ul class="w3-ul w3-hoverable w3-large" id="list">
					<li v-for="recipe in recipes"><a class="w3-button" v-on:click="selectRecipe(recipe)">{{ recipe.title }}</a></li>
				</ul>
				<div class="w3-bar w3-center" id="paging">
					<button v-bind:disabled="pageNo == 0" v-on:click="prevPage" class="w3-button w3-xxlarge" title="previous">&laquo;</button>
					<button v-on:click="nextPage" class="w3-button w3-xxlarge" title="next">&raquo;</button>
				</div>
			</div>
		</div>
		<div class="w3-twothird w3-container">
			<p id="choice">Wähle ein Rezept aus der Liste aus ...</p>
			
			<!-- ************************** einzelnes Rezept anzeigen ********************* -->
			<div id="recipe">
			    <h1>{{ recipe.title }}</h1>
			    <p v-html="recipe.preamble"></p>
			    <h3>Zutaten für <span id="noofperson">{{ recipe.noOfPerson }}</span> Personen</h3>
			    <div id="ingredients">
				    <table class="w3-table w3-bordered">
				    	<tr v-for="ingredient in ingredients"><td>{{ ingredient.portion }}</td><td>{{ ingredient.description }} {{ ingredient.description }}</td></tr>
				    </table>
			    </div>
			    <h3>Zubereitung</h3>
			    <p v-html="recipe.preparation"></p>
			    <p>Rating <span class="w3-badge w3-teal" id="rating">{{ recipe.rating }}</span></p>
			    <p class="w3-tiny">
			    	Erfasst: <span id="adding">{{ formatDate(recipe.addingDate) }}</span><br>
			    	Aktualisiert: <span id="update">{{ formatDate(recipe.editingDate) }}</span><br>
			    </p>
			    <p class="w3-tiny">Stichworte:&nbsp;<span v-for="tag in recipe.tags"><span class='w3-tag w3-teal'>{{ tag }}</span>&nbsp;</span></p>
		    </div>
		</div>
	</div>

	<script>
		var $rootUrl = "/spring-jpa-rest"
		var $recipesUrl = $rootUrl + "/recipes";
		
		var recipesStorage = ( function() {
			var url = $recipesUrl;
					
			var load = function( pageNo, pageSize ) {
				var deferred = $.Deferred();
				var completeUrl = url + "?sort=title&page=" + pageNo + "&size=" + pageSize;
				$.getJSON( completeUrl )
					.fail( function(xhr, status, error) {
						var err = "Request Failed: " + status + ", " + xhr.status + ", " + error;
						console.log( err );
						deferred.reject( err );
					})
					.then( function( json ) {
						deferred.resolve( json.content );
					})
				
				return deferred.promise();
			};

			return {
				load: load
			}
		})();
		
		var recipeStorage = ( function() {
			var load = function( url ) {
				var deferred = $.Deferred();
				$.getJSON( url )
					.done( function(recipe) {
						deferred.resolve( recipe );
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
		
		var formatDate = function( number ) {
			var $myDate = new Date(number);
			return $myDate.toLocaleString();
		};
		
		var recipeViewModel = new Vue({
			el: '#recipe',
			data: {
				recipe: {}
			},
			beforeUpdate: function() {
				$( this.$el ).hide();
			},
			updated: function() {
				$( this.$el ).fadeIn( "slow" );
			},
			methods: {
				formatDate: function( number ) {
					var $myDate = new Date(number);
					return $myDate.toLocaleString();
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
					recipeStorage.load( url ).then( function( json ) {
						recipeViewModel.recipe = json;
					});
				}
			}
		});
		var pagingViewModel = new Vue({
			el: '#paging',
			data: {
				pageSize: 10,
				pageNo: 0	
			},
			created: function() {
				console.log( "pagingViewModel created" );
			},
			beforeUpdated: function() {
				$( "#recipes" ).fadeOut( "fast" );
			},
			methods: {
				nextPage: function() {
					this.pageNo++;
					recipesStorage.load(this.pageNo, this.pageSize).then( function( json ) {
						recipesViewModel.recipes = json;
					})
				},
				prevPage: function() {
					if (this.pageNo > 0) {
						this.pageNo--;
						recipesStorage.load(this.pageNo, this.pageSize).then( function( json ) {
							recipesViewModel.recipes = json;
						})
					}
				}
			}
		});
		
		$(function() {
			$( "#recipes" ).hide();
			recipesStorage.load(pagingViewModel.pageNo, pagingViewModel.pageSize).then( function( json ) {
				console.log( json );
				recipesViewModel.recipes = json;
				$( "#recipes" ).show();
			})
		});
		$(document).ajaxStart( function() {
			$( "#loading" ).show();
		});
		$(document).ajaxStop( function() {
			$( "#loading" ).hide();
		});
	</script>

</body>
</html>