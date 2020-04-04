(this["webpackJsonpproject-ui"]=this["webpackJsonpproject-ui"]||[]).push([[0],{18:function(e,t,a){e.exports=a(43)},23:function(e,t,a){},42:function(e,t,a){},43:function(e,t,a){"use strict";a.r(t);var n=a(0),l=a.n(n),o=a(12),c=a.n(o),i=(a(23),a(13)),s=a(14),r=a(16),u=a(15),d=a(17),m=a(2),h=a.n(m),f=function(e){return l.a.createElement("div",{className:"container"},l.a.createElement("div",{className:"row"},l.a.createElement("div",{className:"col-md-6"},l.a.createElement("form",{onSubmit:e.onFormSubmit},l.a.createElement("h1",null,"File Upload"),l.a.createElement("input",{type:"file",onChange:e.onChange}),l.a.createElement("div",null,l.a.createElement("button",{id:"uploadButton",type:"submit"},"Upload"))))))},p=function(e){return l.a.createElement("div",{className:"col-md-6"},l.a.createElement("ul",{className:"list-group list-group-flush"},l.a.createElement("li",{className:"list-group-item"},e.result)))},E=function(e){function t(){var e;return Object(i.a)(this,t),(e=Object(r.a)(this,Object(u.a)(t).call(this))).show=function(e){return{showResults:!1}},e.onChange=function(t){e.setState({file:t.target.files[0]})},e.onFormSubmit=function(t){t.preventDefault();var a=new FormData;a.append("file",e.state.file),h.a.post("http://localhost:8080/file/uploadFile",a).then((function(t){h.a.get("http://localhost:8080/file/allFilesNames").then((function(t){e.setState({results:t.data})})).catch((function(e){}))})).catch((function(e){alert("You haven't uploaded any files, please try again.")}))},e.onClick=function(t){t.preventDefault(),h.a.post("http://localhost:8080/file/writeToFile").then((function(t){e.setState({showResults:!0}),alert("Words in your files were counted successfully. If you'd like to get your results, please press Download button.")})).catch((function(e){alert("Program was not able to count words. Please check if you uploaded any files.")}))},e.handleClick=function(t){t.preventDefault(),h.a.delete("http://localhost:8080/file/delete").then((function(t){h.a.get("http://localhost:8080/file/allFilesNames").then((function(t){e.setState({results:t.data}),alert("You have deleted all files successfully. \nYou can upload new files.")})).catch((function(e){}))})).catch((function(e){}))},e.downloadFiles=function(){fetch("http://localhost:8080/downloadZip").then((function(e){e.blob().then((function(e){var t=window.URL.createObjectURL(e),a=document.createElement("a");a.href=t,a.download="Results.zip",a.click()}))}))},e.state={file:null,results:[]},e}return Object(d.a)(t,e),Object(s.a)(t,[{key:"render",value:function(){var e=this.state.results.map((function(e,t){return l.a.createElement(p,{key:t,result:e})}));return l.a.createElement("div",{className:"container"},l.a.createElement(f,{file:this.state.file,onChange:this.onChange,onFormSubmit:this.onFormSubmit}),l.a.createElement("hr",null),l.a.createElement("h3",{id:"upload"},"You have uploaded these files: "),e,l.a.createElement("hr",{id:"hr"}),l.a.createElement("div",{className:"row",id:"newRow"},l.a.createElement("h3",{id:"canDo"},"You can:"),l.a.createElement("div",{className:"col-2",id:"countWords"},l.a.createElement("button",{id:"btn",onClick:this.onClick}," Count words")),l.a.createElement("div",{className:"col-1"},l.a.createElement("h3",null,"or")),l.a.createElement("div",{className:"col-3",id:"countWords"},l.a.createElement("button",{onClick:this.handleClick},"Delete present files"))),l.a.createElement("hr",{id:"hr"}),l.a.createElement("div",null,this.state.showResults?l.a.createElement("div",null,l.a.createElement("h4",{className:"download"},"If you'd like to download files with the results, please press the download button below:"),l.a.createElement("button",{className:"download",onClick:this.downloadFiles},"Download")):null))}}]),t}(n.Component),w=function(e){return l.a.createElement("div",{className:"jumbotron jumbotron-fluid"},l.a.createElement("div",{className:"container"},l.a.createElement("h1",{className:"display-4"},"Word counting APP")))};var v=function(){return l.a.createElement("div",null,l.a.createElement(w,null),l.a.createElement(E,null))};Boolean("localhost"===window.location.hostname||"[::1]"===window.location.hostname||window.location.hostname.match(/^127(?:\.(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}$/));a(41),a(42);c.a.render(l.a.createElement(v,null),document.getElementById("root")),"serviceWorker"in navigator&&navigator.serviceWorker.ready.then((function(e){e.unregister()}))}},[[18,1,2]]]);
//# sourceMappingURL=main.b65fd1e2.chunk.js.map