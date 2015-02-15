require 'test_helper'

class StudentsQuizzesControllerTest < ActionController::TestCase
  setup do
    @students_quiz = students_quizzes(:one)
  end

  test "should get index" do
    get :index
    assert_response :success
    assert_not_nil assigns(:students_quizzes)
  end

  test "should get new" do
    get :new
    assert_response :success
  end

  test "should create students_quiz" do
    assert_difference('StudentsQuiz.count') do
      post :create, students_quiz: {  }
    end

    assert_redirected_to students_quiz_path(assigns(:students_quiz))
  end

  test "should show students_quiz" do
    get :show, id: @students_quiz
    assert_response :success
  end

  test "should get edit" do
    get :edit, id: @students_quiz
    assert_response :success
  end

  test "should update students_quiz" do
    patch :update, id: @students_quiz, students_quiz: {  }
    assert_redirected_to students_quiz_path(assigns(:students_quiz))
  end

  test "should destroy students_quiz" do
    assert_difference('StudentsQuiz.count', -1) do
      delete :destroy, id: @students_quiz
    end

    assert_redirected_to students_quizzes_path
  end
end
