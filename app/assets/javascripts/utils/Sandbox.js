/* global define:false */
define([], function () {
  function sandbox() {
    //return 'Ok: ' + 'Yeah!';
    throw new Error('!');
  }

  return {
    go: function() {
      var startTime = new Date().getTime();
      var result = sandbox();
      var elapsed = new Date().getTime() - startTime;
      console.log('Sandbox executed in [' + elapsed + 'ms] with result [' + result + '].');
      return result;
    }
  };
});
