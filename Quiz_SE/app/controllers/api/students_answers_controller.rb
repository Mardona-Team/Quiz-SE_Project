module API

  class StudentsAnswersController < ApplicationController
    # GET /syudents_answers
    # GET /students_answers.json
    def index
      @answers = Answer.all
    end

    # GET /students_answers/1
    # GET /students_answers/1.json
    def show
    end

    # POST /students_answers
    # POST /students_answers.json
    def create
      @user = User.find(params[:id])
      @user.answer_ids = params[:answers]

      respond_to do |format|
          format.json { render json: { marks: @user.answer_quiz(params[:answers], params[:quiz][:id]), total_marks: Quiz.find(params[:quiz][:id]).marks.to_f } }
      end
    end

  end

end