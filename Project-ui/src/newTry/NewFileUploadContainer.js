import React, { Component } from 'react';
import axios from 'axios';
import NewUploadFileComponent from "./NewUploadFileComponent";
import ListForDocumentComponent from "./ListForDocumentsComponent";

class NewUploadFileContainer extends Component {
    constructor() {
        super();
        this.state = {
            file: null,
            results: []
        };
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
                console.log("Pavyko.")
            })
            .catch((error) => {
                console.log("nepavyko.")
            })
        axios.get("http://localhost:8080/file/allFiles")
            .then(response => {
                this.setState({ results: response.data });
            })
            .catch(error => {
                alert("Nėra galimybės pateikti duomenų apie dokumentus.");
            });

    }

    render() {
        let result = this.state.results.map((result, index) => {
            return <ListForDocumentComponent key={index} result={result} />;
        });
        return (
            <div>
                <NewUploadFileComponent
                    file={this.state.file}
                    onChange={this.onChange}
                    onFormSubmit={this.onFormSubmit}
                />
                {result}

                <button type="button" className="btn btn-light">Light</button>
            </div>
        );
    }

}

export default NewUploadFileContainer;
