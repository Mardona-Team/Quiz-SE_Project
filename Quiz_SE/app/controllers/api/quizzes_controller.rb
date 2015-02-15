module API


class QuizzesController < ApplicationController
  before_action :set_quiz, only: [:show, :edit, :update, :destroy]

  # GET /quizzes
  # GET /quizzes.json
  def index
    if params[:group_id]
      @group = Group.find(params[:group_id])
      @quizzes =  Quiz.where(instructor_id: params[:instructor_id]).limit(20)
      render :index
    else
      render json: Quiz.where(instructor_id: params[:instructor_id]).limit(20).as_json(only: [:id, :title], :methods => [:published])
    end
  end

  # GET /quizzes/1
  # GET /quizzes/1.json
  def show
    @quiz=Quiz.find(params[:id])
    render json: @quiz.show_full_details
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
        @quiz.refrence_id = @quiz.id
        @quiz.save
        format.json { render json: @quiz.show_full_details }
      else
        format.json { render json: @quiz.errors, status: :unprocessable_entity }
      end
    end
  end

  # PATCH/PUT /quizzes/1
  # PATCH/PUT /quizzes/1.json
  def update
    respond_to do |format|
      unless Publication.find_by(group_id: params[:group_id], quiz_id: params[:id])
        @quiz.groups << Group.find(params[:group_id])
        format.json { render json: @quiz.show_full_details }
      else
        format.json { render json: { errors: "Either published before or not found"}, status: :unprocessable_entity }
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
      params.require(:quiz).permit(:instructor_id, :title, :subject, :year, :description, :marks, questions_attributes: [:title, right_answer_attributes: [:title], answers_attributes: [:title]])
    end
end

end