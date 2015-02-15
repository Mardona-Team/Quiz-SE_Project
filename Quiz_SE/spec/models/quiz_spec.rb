require 'rails_helper'

RSpec.describe Quiz, :type => :model do

  before(:each) do
    @user = create :user
    questions = create_list(:question, 3, right_answer: create(:answer))
    @quiz = create :quiz, instructor: @user, questions: questions
  end

  context 'attributes' do
    it "has title" do
      @quiz.title = nil
      expect(@quiz).not_to be_valid
    end

    it "has subject" do
      @quiz.subject = nil
      expect(@quiz).not_to be_valid
    end

    it "has year" do
      @quiz.year = nil
      expect(@quiz).not_to be_valid
    end

    it "has marks" do
      @quiz.marks = nil
      expect(@quiz).not_to be_valid
    end

    it "has valid marks" do
      @quiz.marks = -90
      expect(@quiz).not_to be_valid
    end
  end

  context "relationships" do
    it "has an owner" do
      expect(@quiz).to be_valid
    end

    it "is not valid when no owner" do
      @quiz.instructor = nil
      expect(@quiz).not_to be_valid
    end

    it "has questions" do
      expect(@quiz).to be_valid
    end

    it "is not valid when no questions" do
      @quiz.questions.clear
      expect(@quiz).not_to be_valid
    end
  end

  context 'methods' do
    it "has true status when set published" do
      @quiz.set_published
      expect(@quiz.status).to eq(true)
    end

    it "returns '1' when has groups" do
      group = create :group, instructor: @user
      group.quizzes << @quiz
      expect(@quiz.published).to eq("1")
    end

    it "returns '0' when has no groups" do
      group = create :group, instructor: @user
      expect(@quiz.published).to eq("0")
    end

    it "returns '1' when published on group" do
      group = create :group, instructor: @user
      group.quizzes << @quiz
      expect(@quiz.published_on(group)).to eq("1")
    end

    it "returns '0' when not published on group" do
      group = create :group, instructor: @user
      expect(@quiz.published_on(group)).to eq("0")
    end
  end
end
