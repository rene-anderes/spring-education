<%@ page language="java" session="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>

<c:url var="resources" value="/resources"/>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="${ resources }/w3.css">
	<script src="${ resources }/ckeditor5-build-classic/ckeditor.js"></script>
	<title>ckEditor 5</title>
</head>
<body>
	<script type="text/javascript" src="${ resources }/jquery-3.3.1.min.js"></script>
	
	<form class="w3-container" id="recipeForm">
		
		<div class="w3-panel">
			<input class="w3-input" type="text" min="5" max="255" id="title" name="title"> <label class="w3-text-theme">Title</label>
		</div>
		<div class="w3-panel">
			<textarea name="preamble" id="editPreamble"></textarea>
			<label class="w3-text-theme">Preamble</label>
		</div>
		<button type="button" id="showText" class="w3-button" title="ShowText">Show Text</button>
	</form>

	<script type="text/javascript">
	var ckEditor;
	$(function() {
		ClassicEditor
            .create( document.querySelector( '#editPreamble' ), {
            	removePlugins: [ 'Heading', 'Link' ],
        		toolbar: [ 'bold', 'italic', 'bulletedList', 'numberedList', 'blockQuote' ]
            } )
            .then(editor => {
            	console.log(Array.from( editor.ui.componentFactory.names() ));
        		ckEditor = editor;    	
            } )
            .catch( error => {
                console.error( error );
            } );

		$( "#showText" ).click( function() {
			console.log("Editordata: " + ckEditor.getData() );	
		});
	});
	</script>

</body>
</html>