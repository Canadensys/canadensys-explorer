/****************************
Copyright (c) 2013 Canadensys
Boostrapper for modules

DESIGN TEMPLATE FOR MODULES IN THE EXPLORER

  EXPLORER.myModule = (function() {
    "use strict";
    var _private = {
      init: function() {
      //do stuff
      }
    }
    //return some private methods, init will be automatically executed
    return {
      init: _private.init();
    };
  }());
****************************/
/*global window, document, jQuery*/

;(function(ex, $, window, document, undefined) {
  
  'use strict';

  ex.i18n = {

    languageResources: {},

    setLanguageResources: function(resources) {
      if(typeof resources !== 'object') { return false; }
      $.extend(true, this.languageResources, resources);
    },

    getLanguageResource: function(resource) {
      if(this.languageResources.hasOwnProperty(resource)) {
        return this.languageResources[resource];
      }
      return null;
    }

  };

  //global initializer
  $(function() {
    $.each(ex, function() {
      if(this.init) { this.init(); }
    });
  });

}(window.EXPLORER = window.EXPLORER || {}, jQuery, window, document));