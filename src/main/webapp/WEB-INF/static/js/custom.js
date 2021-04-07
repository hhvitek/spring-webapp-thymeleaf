/**


 */

$( 'document' ).ready( function () {

    $( '.goback' ).on( 'click', function (event) {
        event.preventDefault();

        window.history.back();
    });

});