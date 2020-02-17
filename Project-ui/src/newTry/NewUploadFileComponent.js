import React from 'react';

const NewUploadFileComponent = props => {
    return (
        <div className="container">
            <div className="row">
                <div className="col-md-6">
                    <form onSubmit={props.onFormSubmit}>
                        <h1>File Upload</h1>
                        <input type="file" onChange={props.onChange} />
                        <button type="submit">Upload</button>
                    </form>
                </div>
            </div>
        </div>


    )
}

export default NewUploadFileComponent;