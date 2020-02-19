import React, { Component } from 'react';
import axios from 'axios';
import NewUploadFileComponent from "./NewUploadFileComponent";
import ListForDocumentComponent from "./ListForDocumentsComponent";
import JSZip from 'jszip';

class NewUploadFileContainer extends Component {
    constructor() {
        super();
        this.state = {
            file: null,
            results: [],
            content: 'abc',
        };
        console.log(this.state.content)
        console.log(this.state.file)
    }



    onChange = (e) => {
        this.setState({ file: e.target.files[0] })
    }

    onFormSubmit = (e) => {
        e.preventDefault();
        const data = new FormData()
        data.append("file", this.state.file)
        console.log("Info", data)
        axios
            .post("http://localhost:8080/file/uploadFile", data)
            .then((response) => {
                axios.get("http://localhost:8080/file/allFilesNames")
                    .then(response => {
                        this.setState({ results: response.data });
                    })
                    .catch(error => {
                        alert("Nėra galimybės pateikti duomenų apie dokumentus.");
                    });
            })
            .catch((error) => {
                console.log("nepavyko.")
            })


    }

    onClick = (e) => {
        e.preventDefault();
        axios.post("http://localhost:8080/file/writeToFile")
            .then(response => {
                console.log("Pavyko irasyti teksta.")

                // axios.get("http://localhost:8080/file/content")
                //     .then(response => {
                //         console.log(this.setState({ content: response.data }))
                //     })
                //     .catch(error => { "Nepavyko" });
            })
            .catch(error => { "Nepavyko irasyti teksto." });

        // filenames.forEach(filename => {
        //     axios.get("http://localhost:8080/downloadFile/" + filename)
        //         .then(response => { })
        //         .catch(error => {
        //             console.log("negali ikelti.")
        //         });
        // });

    };

    handleClick = (e) => {
        e.preventDefault();
        axios.delete("http://localhost:8080/file/delete")
            .then(response => {
                axios.get("http://localhost:8080/file/allFilesNames")
                    .then(response => {
                        this.setState({ results: response.data });
                    })
                    .catch(error => {
                        alert("Nėra galimybės pateikti duomenų apie dokumentus.");
                    });
                console.log("Sekmingai istrinta")
            })
            .catch(error => { });

    }

    downloadFiles = () => {
        fetch('http://localhost:8080/downloadZip')
            .then(response => {
                console.log(response);
                response.blob().then(blob => {
                    let url = window.URL.createObjectURL(blob);
                    let a = document.createElement('a');
                    a.href = url;
                    a.download = 'zip.zip';
                    a.click();
                });
                //window.location.href = response.url;
            });
    }



    render() {
        let result = this.state.results.map((result, index) => {
            return <ListForDocumentComponent key={index} result={result} />;
        });
        return (
            <div className="container" >
                <NewUploadFileComponent
                    file={this.state.file}
                    onChange={this.onChange}
                    onFormSubmit={this.onFormSubmit}
                />

                <hr></hr>

                <h3>You have uploaded these files: </h3>
                {result}

                <button type="button" className="btn btn-light" onClick={this.onClick} > Count words</button>

                <button type="button" className="btn btn-light" onClick={this.handleClick}>Start all over again</button>


                <h1>Download File using React App</h1>
                <h3>Download Employee Data using Button</h3>
                <button onClick={this.downloadFiles}>Download</button>


            </div >
        );
    }

}

export default NewUploadFileContainer;
