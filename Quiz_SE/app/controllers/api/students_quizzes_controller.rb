class StudentsQuizzesController < ApplicationController
  before_action :set_students_quiz, only: [:show, :edit, :update, :destroy]

  respond_to :html

  def index
    @students_quizzes = StudentsQuiz.all
    respond_with(@students_quizzes)
  end

  def show
    respond_with(@students_quiz)
  end

  def new
    @students_quiz = StudentsQuiz.new
    respond_with(@students_quiz)
  end

  def edit
  end

  def create
    @students_quiz = StudentsQuiz.new(students_quiz_params)
    @students_quiz.save
    respond_with(@students_quiz)
  end

  def update
    @students_quiz.update(students_quiz_params)
    respond_with(@students_quiz)
  end

  def destroy
    @students_quiz.destroy
    respond_with(@students_quiz)
  end

  private
    def set_students_quiz
      @students_quiz = StudentsQuiz.find(params[:id])
    end

    def students_quiz_params
      params[:students_quiz]
    end
end
