module API


  class UsersController < ApplicationController
    before_action :set_user, only: [:show, :edit, :update, :destroy]

  # GET /users
  # GET /users.json
  def index
    if params[:quiz_id]
      @students_quizzes = Quiz.find(params[:quiz_id]).students_quizzes
      render json: @students_quizzes.as_json(only: [:marks], :include => { student: { only: [:id], :methods => [:full_name] }, quiz: { only: [:marks] }})
    else
      @users = User.all
      render json: @users
    end
  end

  # GET /users/1
  # GET /users/1.json
  def show
    if params[:quiz_id]
      @quiz = Quiz.find(params[:quiz_id])
      @students_quiz = StudentsQuiz.find_by(quiz_id: params[:quiz_id], student_id: params[:id])
      render json: {
        student: {
          id: @user.id,
          full_name: @user.full_name,
          marks: @students_quiz.marks
        },
        quiz: {
          id: @quiz.id,
          title: @quiz.title,
          marks: @quiz.marks,
          questions: 
            @quiz.questions.as_json(only: [:id, :title], :include => { right_answer: { only: [:id, :title] } }),
          answers: 
            @user.answers.joins(:question).where(questions: { quiz_id: @quiz.id }).as_json(only: [:id, :title, :question_id])
        }
      }
    else
      render json: @user
    end
  end

  # GET /users/new
  def new
    @user = User.new
  end

  # GET /users/1/edit
  def edit
    @user=User.find(params[:id])
    render json: @user
  end

  # POST /users
  # POST /users.json
  def create
    @user = User.new(user_params)

    respond_to do |format|
      if @user.save
        format.json { render json: @user }
      else
        format.json { render json: @user.errors, status: :unprocessable_entity }
      end
    end
  end

  # PATCH/PUT /users/1
  # PATCH/PUT /users/1.json
  def update
    respond_to do |format|
      if @user.update(user_params)
        format.html { redirect_to @user, notice: 'User was successfully updated.' }
        format.json { render :show, status: :ok, location: @user }
      else
        format.html { render :edit }
        format.json { render json: @user.errors, status: :unprocessable_entity }
      end
    end
  end

  # DELETE /users/1
  # DELETE /users/1.json
  def destroy
    @user.destroy
    respond_to do |format|
      format.html { redirect_to users_url, notice: 'User was successfully destroyed.' }
      format.json { head :no_content }
    end
  end

  private
    # Use callbacks to share common setup or constraints between actions.
    def set_user
      @user = User.find(params[:id])
    end

    # Never trust parameters from the scary internet, only allow the white list through.
    def user_params
      params.require(:user).permit(:username, :first_name, :last_name, :password, :email, :type, :faculty, :university, :department, :year)
    end
  end

end
