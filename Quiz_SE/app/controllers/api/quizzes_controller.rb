module API


class QuizzesController < ApplicationController
  before_action :set_quiz, only: [:show, :edit, :update, :destroy]

  # GET /quizzes
  # GET /quizzes.json
  def index
    @quizzes = Quiz.all
    render json: @quizzes.limit(20).as_json(only: [:id, :title])
  end

  # GET /quizzes/1
  # GET /quizzes/1.json
  def show
    @quiz=Quiz.find(params[:id])
    render json: @quiz.as_json(:only => [:id, :title, :subject, :year, :description, :marks, :created_ar],
                                :include => {
                                  :questions => {:only => [:id, :title],
                                      :methods => [:shuffled_answers]
                                    },
                                })
  end

  # GET /quizzes/new
  def new
    @quiz = Quiz.new
  end

  # GET /quizzes/1/edit
  def edit
    @quiz=Quiz.find(params[:id])
    render json: @quiz
  end

  # POST /quizzes
  # POST /quizzes.json
  def create
    @quiz = Quiz.new(quiz_params)

    respond_to do |format|
      if @quiz.save
        format.json { render :show, id: @quiz.id }
      else
        format.json { render json: @quiz.errors, status: :unprocessable_entity }
      end
    end
  end

  # PATCH/PUT /quizzes/1
  # PATCH/PUT /quizzes/1.json
  def update
    respond_to do |format|
      if @quiz.update(quiz_params)
        format.json { render :show, id: @quiz.id }
      else
        format.json { render json: @quiz.errors, status: :unprocessable_entity }
      end
    end
  end

  # DELETE /quizzes/1
  # DELETE /quizzes/1.json
  def destroy
    @quiz.destroy
    respond_to do |format|
      format.json { head :no_content }
    end
  end

  private
    # Use callbacks to share common setup or constraints between actions.
    def set_quiz
      @quiz = Quiz.find(params[:id])
    end

    # Never trust parameters from the scary internet, only allow the white list through.
    def quiz_params
      params.require(:quiz).permit(:title, :subject, :year, :description, :marks, questions_attributes: [:title, right_answer_attributes: [:title], answers_attributes: [:title]])
    end
end

end