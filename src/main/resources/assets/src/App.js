import React, { Component } from 'react';
import { BrowserRouter as Router, Route, Link } from "react-router-dom";

import './App.css';
import Question from './Question';
import Form from './Form';

class App extends Component {
  state = {
    questions: {}
  }

  componentDidMount() {
    this.fetchQuestions();
  }

  makeQuestionMap = (data) => {
    return data.reduce((acc, next) => {
        if (acc[next.course]) {
            if (acc[next.course][next.subject]) {
                acc[next.course][next.subject].push({text: next.text, id: next.id});
            } else {
                acc[next.course][next.subject] = [];
                acc[next.course][next.subject].push({text: next.text, id: next.id});
            }
        } else {
            acc[next.course] = {[next.subject]: [{text: next.text, id: next.id}]};
        }

        return acc;
    }, {});
  }

  fetchQuestions = () => {
    return fetch('/api/questions')
      .then(res => res.json())
      .then(this.makeQuestionMap)
      .then(questions => this.setState({ questions }));
  }

  renderQuestions = () => {
    if (Object.keys(this.state.questions).length === 0) {
        return <ul></ul>;
    }

    return (
        <ul>
          {Object.keys(this.state.questions).map((course, index) => {
            return (
                <li key={index}>
                    <h3>{course}</h3>
                    {Object.keys(this.state.questions[course]).map((subject, index) => {
                      return (
                          <div key={index}>
                              <h4>{subject}</h4>
                              {this.state.questions[course][subject].map((question => {
                                return (
                                  <Link key={index} to={`/question/${question.id}`}>
                                      {question.text}
                                  </Link>
                                );
                              }))}
                          </div>
                      );
                    })}
                </li>
            );
          })}
        </ul>
    );
  }

  render() {
    return (
      <Router>
        <div className="App">
          <div className="App-sidebar">
            {this.renderQuestions()}
          </div>
          <div className="App-content">
              <Route path="/question/:id" component={(props) => <Question fetchQuestions={this.fetchQuestions} {...props}/>} />
              <Form fetchQuestions={this.fetchQuestions}/>
          </div>
        </div>
      </Router>
    );
  }
}

export default App;
