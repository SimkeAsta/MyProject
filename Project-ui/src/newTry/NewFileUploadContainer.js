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
            filename: ''
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
        axios.get("http://localhost:8080/file/allFilesNames")
            .then(response => {
                this.setState({ results: response.data });
            })
            .catch(error => {
                alert("Nėra galimybės pateikti duomenų apie dokumentus.");
            });

    }

    onClick = (e) => {
        e.preventDefault();
        axios.post("http://localhost:8080/file/writeToFile")
            .then(response => {
                console.log("Pavyko irasyti teksta.")
            })
            .catch(error => { "Nepavyko irasyti teksto." });
        axios.get("http://localhost:8080/file/download")
            .then(response => {
                console.log()
            })
            .catch(error => { "Nepavyko" });
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
                console.log("Sekmingai istrinta")
            })
            .catch(error => { });
        axios.get("http://localhost:8080/file/allFilesNames")
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
            <div className="container">
                <NewUploadFileComponent
                    file={this.state.file}
                    onChange={this.onChange}
                    onFormSubmit={this.onFormSubmit}
                />

                <hr></hr>

                <h3>You have uploaded these files: </h3>
                {result}

                <button type="button" className="btn btn-light" onClick={this.onClick}>Count words</button>

                <button type="button" className="btn btn-light" onClick={this.handleClick}>Start all over again</button>

            </div>
        );
    }

}

export default NewUploadFileContainer;
