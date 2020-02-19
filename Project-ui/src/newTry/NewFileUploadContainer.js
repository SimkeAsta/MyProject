import React, { Component } from 'react';
import axios from 'axios';
import NewUploadFileComponent from "./NewUploadFileComponent";
import ListForDocumentComponent from "./ListForDocumentsComponent";

class NewUploadFileContainer extends Component {
    constructor() {
        super();
        this.state = {
            file: null,
            results: [],
        };
    }

    show = (e) => {
        return { showResults: false };
    }

    onChange = (e) => {
        this.setState({ file: e.target.files[0] })
    }

    onFormSubmit = (e) => {
        e.preventDefault();
        const data = new FormData()
        data.append("file", this.state.file)
        axios
            .post("http://localhost:8080/file/uploadFile", data)
            .then((response) => {
                axios.get("http://localhost:8080/file/allFilesNames")
                    .then(response => {
                        this.setState({ results: response.data });
                    })
                    .catch(error => {
                    });
            })
            .catch((error) => {
                alert("You haven't uploaded any files, please try again.")
            })
    }

    onClick = (e) => {
        e.preventDefault();
        axios.post("http://localhost:8080/file/writeToFile")
            .then(response => {
                this.setState({ showResults: true });
                alert("Words in your files were counted successfully. If you'd like to get your results, please press Download button.")
            })
            .catch(error => {
                alert("Program was not able to count words. Please check if you uploaded any files.")
            })
    };

    handleClick = (e) => {
        e.preventDefault();
        axios.delete("http://localhost:8080/file/delete")
            .then(response => {
                axios.get("http://localhost:8080/file/allFilesNames")
                    .then(response => {
                        this.setState({ results: response.data });
                        alert("You have deleted all files successfully. \nYou can upload new files.")
                    })
                    .catch(error => {
                    });
            })
            .catch(error => { });

    }

    downloadFiles = () => {
        fetch('http://localhost:8080/downloadZip')
            .then(response => {
                response.blob().then(blob => {
                    let url = window.URL.createObjectURL(blob);
                    let a = document.createElement('a');
                    a.href = url;
                    a.download = 'Results.zip';
                    a.click();
                });
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

                <h3 id="upload">You have uploaded these files: </h3>
                {result}

                <hr id="hr"></hr>

                <div className="row" id="newRow">
                    <h3 id="canDo">You can:</h3>
                    <div className="col-2" id="countWords">
                        <button id="btn" onClick={this.onClick} > Count words</button>
                    </div>
                    <div className="col-1">
                        <h3>or</h3>
                    </div>
                    <div className="col-3" id="countWords">
                        <button onClick={this.handleClick}>Delete present files</button>
                    </div>
                </div>

                <hr id="hr"></hr>
                <div>
                    {this.state.showResults ?
                        <div>
                            <h4 className="download">If you'd like to download files with the results, please press the download button below:</h4>
                            <button className="download" onClick={this.downloadFiles}>Download</button>
                        </div>
                        : null}
                </div>
            </div >
        );
    }

}

export default NewUploadFileContainer;
