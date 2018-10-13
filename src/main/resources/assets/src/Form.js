import React, { Component } from 'react';

import './Form.css';

class Form extends Component {
  state = {
    subject: '',
    text: '',
    course: ''
  }

  addQuestion = (e) => {
    e.preventDefault();

    const data = new URLSearchParams();
    data.append('subject', this.state.subject);
    data.append('course', this.state.course);
    data.append('text', this.state.text);

    return fetch('/api/questions', {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
      },
      mode: 'no-cors',
      body: data.toString()
    }).then(() => {
      this.props.fetchQuestions();
      this.setState({subject: '', text: '', course: ''});
    });
  }

  render() {
    return (
      <form className="Form" onSubmit={this.addQuestion}>
        <legend>Lisää Kysymys</legend>
        <div>
            <label>Aihe:</label>
            <input value={this.state.subject} onChange={(e) => this.setState({subject: e.target.value})} required placeholder="Aihe" type="text" />
        </div>
        <div>
            <label>Kurssi</label>
            <input value={this.state.course} onChange={(e) => this.setState({course: e.target.value})} required placeholder="Kurssi" type="text"/>
        </div>
        <div>
            <label>Kysymys:</label>
            <textarea rows="5" cols="50" value={this.state.text} onChange={(e) => this.setState({text: e.target.value})} required></textarea>
        </div>
        <button>Lisää</button>
      </form>
    );
  }
}

export default Form;
