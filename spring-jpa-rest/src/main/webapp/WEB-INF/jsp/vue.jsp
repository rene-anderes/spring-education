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

	<div class="w3-third w3-container">
		<h3>Liste aller Rezepte <span id="loading"><i class="fa fa-refresh"></i></span></h3>
		<div id="recipes">
			<ul class="w3-ul w3-hoverable w3-large" id="list">
				<li v-for="recipe in recipes"><a href="#" v-on:click="selectRecipe(recipe)">{{ recipe.title }}</a></li>
			</ul>
			<div class="w3-bar w3-center">
				<a id="prevPage" class="w3-button w3-xxlarge" title="previous">&laquo;</a>
				<a id="nextPage" class="w3-button w3-xxlarge" title="next">&raquo;</a>
			</div>
		</div>
	</div>
	
	<script>
		var $rootUrl = "/spring-jpa-rest"
		var $recipesUrl = $rootUrl + "/recipes";
		
		var recipes = (function() {
			var url = $recipesUrl;
			var pageSize = 10;
			var pageNo = 0;
			var showRecipeCallback;
					
			var load = function() {
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
		
		var recipesViewModel = new Vue({
			el: '#recipes',
			data: { 
				recipes: {} 
			},
			beforeUpdated: function() {
				$( "#recipes" ).fadeOut( "fast" );
			},
			updated: function() {
				$( "#recipes" ).fadeIn( "fast" );
			},
			methods: {
				selectRecipe: function( recipe ) {
					alert("recipe: " + recipe);
					var url = "";
					$.each(recipe.links, function(idx, link) {
						if (link.rel == "self") {
							url = link.href;
						}
					});
					alert("url: " + url);
				}
			}
		})
		$(function() {
			$( "#recipes" ).hide();
			recipes.load().then( function( json ) {
				console.log( json );
				recipesViewModel.recipes = json;
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