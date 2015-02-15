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

      unless (StudentsQuiz.find_by(student_id: params[:id], publication_id: params[:quiz][:id]))
        respond_to do |format|
            format.json { render json: { marks: @user.answer_quiz(params[:answers], params[:quiz][:id]), total_marks: Publication.find(params[:quiz][:id]).quiz.marks.to_f } }
        end
      else
        respond_to do |format|
          format.json { render json: { errors: "You have answered before" }, status: :unprocessable_entity }
        end
      end
    end

  end

end