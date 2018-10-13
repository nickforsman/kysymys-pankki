import React, { Component } from 'react';

import './Question.css';

class Question extends Component {
  state = {
    question: {},
    answers: [],
    answer: '',
    correct: false
  }

  componentDidMount() {
    this.fetchQuestion();
  }

  componentDidUpdate(prevProps, prevState) {
    if (this.props.match.params.id !== prevProps.match.params.id) {
      this.fetchQuestion();
    }
  }

  fetchQuestion() {
    return fetch(`/api/questions/${this.props.match.params.id}`)
      .then(res => res.json())
      .then(question => {
        this.setState({question})
        return fetch(`/api/questions/${this.props.match.params.id}/answers`)
      })
      .then(res => res.json())
      .then(answers => {
        this.setState({answers});
      });
  }

  deleteQuestion = () => {
    const yes = window.confirm("Oletko varma?");

    if (yes) {
        fetch(`/api/questions/${this.props.match.params.id}`, {
          method: 'DELETE'
        })
        .then(() => {
            this.props.fetchQuestions();
            this.props.history.push("/");
        });
    }
  }

  deleteAnswer = (id) => {
    fetch(`/api/questions/${this.props.match.params.id}/answers/${id}`, {
      method: 'DELETE'
    })
    .then(() => this.fetchQuestion());
  }

  addAnswer = (e) => {
    e.preventDefault();

    const data = new URLSearchParams();
    data.append('answer', this.state.answer);
    data.append('correct', this.state.correct);

    fetch(`/api/questions/${this.props.match.params.id}/answers`, {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
      },
      body: data.toString()
    })
    .then(() => this.fetchQuestion())
    .then(() => this.setState({answer: '', correct: false}));
  }

  render() {
    return (
      <div className="Question">
        <div className="Question-header">
            <div>
                <h2>Kysymys:</h2>
                <p className="Question-text">{this.state.question.text}</p>
            </div>
            <button className="Question-delete" onClick={this.deleteQuestion}>Poista Kysymys</button>
        </div>
        <div className="Question-answers">
            <h3>Vastaukset:</h3>
            {this.state.answers.map((i, key) => {
              return <div key={key}>
                <span>
                  {i.text}
                </span>
                <button onClick={this.deleteAnswer.bind(null, i.id)}>X</button>
              </div>
            })}
        </div>
        <hr />
        <form className="Question-form" onSubmit={this.addAnswer}>
          <legend>Lisää Vastaus</legend>
          <div>
            <label>Vastaus</label>
            <input placeholder="vastaus" value={this.state.answer} required onChange={(e) => this.setState({answer: e.target.value})} />
          </div>
          <div>
            <label>
              Oikea vastaus
              <input type="checkbox" onChange={(e) => this.setState({correct: e.target.value === "on"})} />
            </label>
          </div>
          <button>Lisää</button>
        </form>
        <hr />
      </div>
    );
  }
}

export default Question;
