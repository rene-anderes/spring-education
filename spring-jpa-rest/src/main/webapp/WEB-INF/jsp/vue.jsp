<%@ page language="java" session="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>

<c:url var="resources" value="/resources"/>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js" crossorigin="anonymous"></script>
	<script type="text/javascript" src="${ resources }/jquery-3.3.1.min.js"></script>
	<script src="${ resources }/cookbookStorage.js" type="text/javascript"></script>
	<link rel="stylesheet" href="${ resources }/w3.css">
	<link rel="stylesheet" href="${ resources }/font-awesome-4.7.0/css/font-awesome.min.css">
	<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Proza+Libre">
	<link rel="stylesheet" href="https://www.w3schools.com/lib/w3-theme-dark-grey.css">
	<style>
		html, body, h1, h2, h3, h4, h5, h6 {
		    font-family: "Proza Libre", Verdana, Helvetica, sans-serif;
		}
	</style>
	<title>cookbookAPI with vue.js</title>
</head>
<body class="w3-theme-d5">
	<script>
		const EventBus = new Vue();
		Object.defineProperties(Vue.prototype, {
			$eventbus: {
				get: function () {
					return EventBus
				}
			}
		});
		
		const pagingComponent = {
			data: function() {
				return {
					pageSize: 10,
					pageNo: 0,
					last: false,
					first: true
				}	
			},
			created: function() { 
				this.processLoad();
			},
			methods: {
				nextPage: function() {
					if (!this.last) {
						this.pageNo++;
						this.processLoad();
					}
				},
				prevPage: function() {
					if (this.pageNo > 0) {
						this.pageNo--;
						this.processLoad();
					};
				},
				created: function() { 
					this.$eventbus.$on( 'paging-update', function( json ) {
						this.pageSize = json.size;
						this.last = json.last;
						this.pageNo = json.number;
					}.bind(this));
				},
				processLoad: function() {
					var self = this;
					url = cookbookAPI.getRecipesUrl(this.pageNo, this.pageSize);
					cookbookAPI.load( url ).then( function( json ) {
						EventBus.$eventbus.$emit( 'recipes-update', json.content );
						self.pageSize = json.size;
						self.pageNo = json.number;
						self.last = json.last;
						self.first = json.first;
					}).bind( this );
				} 
			}
		};
		
		const recipesComponent = {
			data: function() {
				return { 
					recipes: {} 
				}
			},
			components: {
				'paging' : pagingComponent	
			},
			beforeUpdate: function() {
				$( this.$el ).hide();
			},
			updated: function() {
				$( this.$el ).fadeIn( "slow" );
			},
			created: function() { 
				this.$eventbus.$on( 'recipes-update', function( json ) {
					this.recipes = json; 
				}.bind(this));
			},
			methods: {
				selectRecipe: function( recipe ) {
	
					var links = $.grep( recipe.links, function( link ) {
						return link.rel == "self";
					});
					var url = links[0].href;
					
					cookbookAPI.load( url ).then( function( json ) {
						EventBus.$eventbus.$emit( 'recipe-update', json );
					})
				}
			}
		};
		
		const ingredientsComponent = {
			data : function() {
				return {
					ingredients : []
				}
			},
			beforeUpdate : function() {
				$(this.$el).hide();
			},
			updated : function() {
				$(this.$el).fadeIn("slow");
			},
			created : function() {
				this.$eventbus.$on('ingredients-update', function(json) {
					this.ingredients = json;
				}.bind(this));
			},
			computed : {
				orderedIngredients : function() {
					return this.ingredients.sort( function( a, b ){
						if (a.description.toLowerCase() > b.description.toLowerCase()) {
							return 1;
						}
						if (a.description.toLowerCase() < b.description.toLowerCase()) {
							return -1;
						}
						return 0;
					});
				}
			}
		};

		const recipeComponent = {
			data : function() {
				return {
					recipe : {}
				}
			},
			components : {
				'ingredients' : ingredientsComponent
			},
			beforeUpdate : function() {
				$(this.$el).hide();
				cookbookAPI.load(this.getIngredientsUrl()).then(function(json) {
					EventBus.$eventbus.$emit('ingredients-update', json);
				});
			},
			updated : function() {
				$(this.$el).fadeIn("slow");
			},
			created : function() {
				EventBus.$eventbus.$on('recipe-update', function(json) {
					this.recipe = json;
				}.bind(this));
			},
			methods : {
				formatDate : function(number) {
					var $myDate = new Date(number);
					return $myDate.toLocaleString();
				},
				getIngredientsUrl : function() {
					var links = $.grep( this.recipe.links, function( link ) {
						return link.rel == "ingredients";
					});
					return links[0].href;
				}
			}
		};
	</script>

	<header class="w3-container w3-theme-l3 w3-margin-bottom">
  		<h1>Kochbuch chez René</h1>
	</header> 
	
	<div id="app" class="w3-row">
		<div class="w3-third w3-container w3-margin-top">
			<div class="w3-container w3-theme">
				<h3>Liste aller Rezepte <span id="loading"><i class="fa fa-refresh"></i></span></h3>
			</div>
			<recipe-list inline-template>
				<div>
					<ul class="w3-ul w3-hoverable w3-large">
						<li v-for="recipe in recipes" :key="recipe.uuid" style="cursor:pointer;" v-on:click="selectRecipe(recipe)" class="w3-display-container">
							{{ recipe.title }}
							<span v-if="false" class="w3-button w3-display-right">
								<i class="fa fa-pencil-square-o w3-right-align" aria-hidden="true"></i>
							</span>
						</li>
					</ul>
					<paging inline-template>
						<div class="w3-bar w3-center">
							<button v-bind:disabled="first" v-on:click="prevPage" class="w3-button w3-xxlarge" title="previous">&laquo;</button>
							<button v-bind:disabled="last" v-on:click="nextPage" class="w3-button w3-xxlarge" title="next">&raquo;</button>
						</div>
					</paging>
				</div>
			</recipe-list>
		</div>
		<div class="w3-twothird w3-container">
			<recipe inline-template>
				<div>
					<p v-if="!recipe.uuid">Wähle ein Rezept aus der Liste aus ...</p>
					<div v-else>
					    <h1>{{ recipe.title }}</h1>
					    <p v-html="recipe.preamble"></p>
					    <h3>Zutaten für <span id="noofperson">{{ recipe.noOfPerson }}</span> Personen</h3>
				    	<ingredients inline-template>
							<table class="w3-table w3-bordered" style="max-width:50%">
								<tr v-for="ingredient in orderedIngredients" :key="ingredient.resourceId">
									<td>{{ ingredient.portion }}</td>
									<td>{{ ingredient.description }}&nbsp;{{ ingredient.comment ? "(" + ingredient.comment + ")" : "" }}</td>
								</tr>
							</table>
						</ingredients>
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
		    </recipe>
		</div>
	</div>
	<p>&nbsp;</p>
	<footer class="w3-container w3-center w3-theme-d5" style="display: none;">
		<p>&copy; 1996-2018 René Anderes | www.anderes.org | design and implemented by René Anderes | Powered by w3.css, jQuery &amp; vue.js</p>
	</footer> 

	<script>
		
		new Vue({
			el: '#app',
			components: {
				'recipe-list' : recipesComponent,
				'recipe' : recipeComponent
			},
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